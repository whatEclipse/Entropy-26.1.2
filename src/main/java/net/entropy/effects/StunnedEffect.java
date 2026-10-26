package net.entropy.effects;

import net.entropy.particles.EntropyParticles;
import net.entropy.registry.EntropyEffects;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class StunnedEffect extends MobEffect {

    public static final int DURATION_TICKS = 20;
    public StunnedEffect() {
        super(MobEffectCategory.HARMFUL, 0xE8D44D);
    }
;
    public static void stun(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(
                EntropyEffects.STUNNED, DURATION_TICKS, 0, false, false, false));

        if (entity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(EntropyParticles.STUN,
                    entity.getX(), entity.getY() + entity.getBbHeight() + 0.3, entity.getZ(),
                    1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        if (entity instanceof Mob mob) {
            makeClueless(mob);
        }
        return true;
    }

    private static void makeClueless(Mob mob) {
        mob.setTarget(null);
        mob.setLastHurtByMob(null);
        mob.getNavigation().stop();
    }

    public static void registerEvents() {
        AttackEntityCallback.EVENT.register((player, level, hand, entity, hitResult) ->
                player.hasEffect(EntropyEffects.STUNNED) ? InteractionResult.FAIL : InteractionResult.PASS);

        ServerLivingEntityEvents.ALLOW_DAMAGE.register((victim, source, amount) ->
                !(source.getEntity() instanceof LivingEntity attacker
                        && attacker.hasEffect(EntropyEffects.STUNNED)));

        ServerLivingEntityEvents.AFTER_DAMAGE.register((victim, source, baseDamage, damageTaken, blocked) -> {
            if (victim instanceof Mob mob && mob.hasEffect(EntropyEffects.STUNNED)) {
                makeClueless(mob);
            }
        });
    }
}