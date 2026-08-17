package net.entropy.items.weapons;

import net.entropy.entity.projectile.ThrownAtlanTridentEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow.Pickup;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AtlanTridentItem extends TridentItem {

    public AtlanTridentItem(Item.Properties properties) {
        super(properties
                .sword(ToolMaterial.GOLD, 9.0F, -2.8F)
                .rarity(Rarity.EPIC)
                .stacksTo(1)
                .enchantable(14)
                .component(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
        );
    }

    @Override
    public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
        if (entity instanceof Player player) {
            int timeHeld = this.getUseDuration(itemStack, entity) - remainingTime;
            if (timeHeld < 10) {
                return false;
            }

            float riptideStrength = EnchantmentHelper.getTridentSpinAttackStrength(itemStack, player);
            if (riptideStrength > 0.0F && (!player.isInWaterOrRain() || player.isPassenger())) {
                return false;
            }

            if (itemStack.nextDamageWillBreak()) {
                return false;
            }

            Holder<SoundEvent> sound = EnchantmentHelper
                    .pickHighestLevel(itemStack, EnchantmentEffectComponents.TRIDENT_SOUND)
                    .orElse(SoundEvents.TRIDENT_THROW);
            player.awardStat(Stats.ITEM_USED.get(this));

            if (level instanceof ServerLevel serverLevel) {
                itemStack.hurtWithoutBreaking(1, player);
                if (riptideStrength == 0.0F) {
                    ItemStack thrownItemStack = itemStack.consumeAndReturn(1, player);
                    ThrownAtlanTridentEntity trident = (ThrownAtlanTridentEntity) Projectile.spawnProjectileFromRotation(
                            ThrownAtlanTridentEntity::new, serverLevel, thrownItemStack, player, 0.0F, 2.5F, 1.0F);

                    if (player.hasInfiniteMaterials()) {
                        trident.pickup = Pickup.CREATIVE_ONLY;
                    }

                    level.playSound(null, trident, sound.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    return true;
                }
            }

            if (riptideStrength > 0.0F) {
                float yRot = player.getYRot();
                float xRot = player.getXRot();
                float xd = -Mth.sin(yRot * ((float) Math.PI / 180F)) * Mth.cos(xRot * ((float) Math.PI / 180F));
                float yd = -Mth.sin(xRot * ((float) Math.PI / 180F));
                float zd = Mth.cos(yRot * ((float) Math.PI / 180F)) * Mth.cos(xRot * ((float) Math.PI / 180F));
                float dist = Mth.sqrt(xd * xd + yd * yd + zd * zd);
                xd *= riptideStrength / dist;
                yd *= riptideStrength / dist;
                zd *= riptideStrength / dist;
                player.push(xd, yd, zd);
                player.startAutoSpinAttack(20, 8.0F, itemStack);
                if (player.onGround()) {
                    player.move(MoverType.SELF, new Vec3(0.0F, 1.1999999F, 0.0F));
                }

                level.playSound(null, player, sound.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                return true;
            }

            return false;
        }

        return false;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (itemInHand.nextDamageWillBreak()) {
            return InteractionResult.FAIL;
        } else if (EnchantmentHelper.getTridentSpinAttackStrength(itemInHand, player) > 0.0F && !player.isInWaterOrRain()) {
            return InteractionResult.FAIL;
        } else {
            player.startUsingItem(hand);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        ThrownAtlanTridentEntity trident = new ThrownAtlanTridentEntity(
                level, position.x(), position.y(), position.z(), itemStack.copyWithCount(1));
        trident.pickup = Pickup.ALLOWED;
        return trident;
    }
    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }
}