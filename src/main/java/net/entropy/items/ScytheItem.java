package net.entropy.items;

import net.entropy.enchantment.EntropyEnchantments;
import net.entropy.entity.BloodScytheEntity;
import net.entropy.sounds.EntropySounds;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;

import java.util.ArrayList;
import java.util.List;

public class ScytheItem extends Item  {
    private static final double PULL_STRENGTH = 0.75;

    public ScytheItem(Item.Properties properties) {
        super(properties
                .sword(ToolMaterial.IRON, 7.0F, -2.9F)
                .durability(1782)
                .rarity(Rarity.EPIC)
                .repairable(ItemTags.IRON_TOOL_MATERIALS)
                .component(DataComponents.TOOL, new Tool(
                        List.of(Tool.Rule.minesAndDrops((HolderSet<Block>) BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK).getOrThrow(BlockTags.MINEABLE_WITH_HOE)
                                , 9.0F)), 1.0F, 1, true)
                )
                .component(DataComponents.WEAPON, new Weapon(
                        1,
                        6.0F
                ))
                .component(DataComponents.ATTACK_RANGE, new AttackRange(1, 3.5F, 1, 3.5F, 1, 1))
        );
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        int spewingLevel = EnchantmentHelper.getItemEnchantmentLevel(
                level.registryAccess().getOrThrow(EntropyEnchantments.SPEWING), stack);

        if (spewingLevel <= 0) {
            return InteractionResult.PASS;
        }

        if (player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            BloodScytheEntity spew = new BloodScytheEntity(level, player);


            spew.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);

            List<MobEffectInstance> halvedEffects = new ArrayList<>();
            float absorption = player.getAbsorptionAmount();
            for (MobEffectInstance effect : player.getActiveEffects()) {
                MobEffectInstance halved = new MobEffectInstance(effect.getEffect(), effect.getDuration() / 2,
                        effect.getAmplifier(), effect.isAmbient(), effect.isVisible(), effect.showIcon());
                spew.addEffect(halved);
                halvedEffects.add(halved);
            }
            player.removeAllEffects();
            for (MobEffectInstance halved : halvedEffects) {
                player.addEffect(halved);
            }
            player.setAbsorptionAmount(absorption);

            player.hurtServer(serverLevel, player.damageSources().magic(), 3.0F);

            level.addFreshEntity(spew);

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    EntropySounds.ITEM_SCYTHE_SPEWING, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        player.getCooldowns().addCooldown(stack, 20);
        return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        attacker.level().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(),
                EntropySounds.ITEM_SCYTHE_HIT, SoundSource.PLAYERS, 1.0F,
                1.0F + attacker.getRandom().nextFloat() / 10f);

        int reelingLevel = EnchantmentHelper.getItemEnchantmentLevel(
                attacker.level().registryAccess().getOrThrow(EntropyEnchantments.REELING), stack);

        if (reelingLevel > 0 && isCriticalHit(attacker)) {
            pullTowardAttacker(target, attacker);
        }
    }

    private boolean isCriticalHit(LivingEntity attacker) {
        return attacker.fallDistance > 0.0F
                && !attacker.onGround()
                && !attacker.onClimbable()
                && !attacker.isInWater()
                && !attacker.isSprinting()
                && !attacker.isPassenger();
    }

    private void pullTowardAttacker(LivingEntity target, LivingEntity attacker) {
        Vec3 diff = attacker.position().subtract(target.position());
        Vec3 horizontal = new Vec3(diff.x, 0, diff.z);

        if (horizontal.lengthSqr() < 1.0E-4) {
            return;
        }

        Vec3 pull = horizontal.normalize().scale(PULL_STRENGTH);
        target.setDeltaMovement(pull.x, 0.1, pull.z);

        if (target instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(target));
        }
    }
}