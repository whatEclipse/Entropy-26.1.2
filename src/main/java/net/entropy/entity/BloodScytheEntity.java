package net.entropy.entity;

import com.google.common.collect.Sets;
import net.entropy.particles.EntropyParticles;
import net.entropy.sounds.EntropySounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow; // relocated into this subpackage in 26.1
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BloodScytheEntity extends AbstractArrow {
    private final Set<MobEffectInstance> effectInstance = Sets.newHashSet();
    public int ticksUntilRemove = 5;
    public final List<LivingEntity> hitEntities = new ArrayList<>();

    // Picked once at spawn and never touched again - getYRot() drifts every tick because
    // AbstractArrow automatically re-aims itself to face the current velocity direction as
    // it flies, which is what was causing the trail to "randomly rotate while moving".
    // Using a fixed value here instead keeps the trail's angle locked for the entity's
    // whole lifetime.
    private final float particleAngle = 35.0F + this.random.nextFloat() * 20.0F;

    public BloodScytheEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public BloodScytheEntity(Level level, LivingEntity owner) {
        // Source calls super(ArsenalEntities.BLOOD_SCYTHE, owner, world) - a 3-arg
        // constructor. That 3-arg constructor doesn't exist on AbstractArrow in 26.1.2
        // (compiler error confirmed this) - it's 5-arg now, with two extra ItemStack
        // params (pickup stack, fired-from-weapon stack). EMPTY/null are the closest
        // equivalent to "no real stack involved", matching asItemStack() returning EMPTY.
        super(EntropyEntities.BLOOD_SCYTHE, owner, level, ItemStack.EMPTY, null);
    }

    public void addEffect(MobEffectInstance effect) {
        this.effectInstance.add(effect);
    }

    // Source: protected ItemStack asItemStack() - renamed in Mojmap.
    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    // Not present in the source at all - this is a second abstract method that only
    // exists on 26.1.2's AbstractArrow (didn't exist in 1.20.1), so it has to be
    // implemented for this to compile. No equivalent to port from the source.
    @Override
    protected ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void tick() {
        super.tick();

        Vec3 velocity = this.getDeltaMovement();

        for (float x = -3; x <= 3; x += 0.1f) {
            this.level().addParticle(EntropyParticles.BLOOD_BUBBLE,
                    this.getX() + x * Math.cos(this.particleAngle), this.getY(), this.getZ() + x * Math.sin(this.particleAngle),
                    velocity.x, velocity.y, velocity.z);
        }

        // Source: this.inGround (a field). 26.1.2 exposes it as isInGround() instead.
        if (this.isInGround() || this.tickCount > 20) {
            for (int i = 0; i < 50; i++) {
                this.level().addParticle(EntropyParticles.BLOOD_BUBBLE_SPLATTER,
                        this.getX() + (this.random.nextGaussian() * 2) * Math.cos(this.particleAngle), this.getY(),
                        this.getZ() + (this.random.nextGaussian() * 2) * Math.sin(this.particleAngle),
                        this.random.nextGaussian() / 10, this.random.nextFloat() / 2, this.random.nextGaussian() / 10);
            }
            this.ticksUntilRemove--;
        }

        if (this.ticksUntilRemove <= 0) {
            this.discard();
        }

        // Source: !this.getWorld().isClient. Same check, renamed.
        // Source: livingEntity.damage(this.getWorld().getDamageSources().create(...), 12.0f).
        // Mojmap's equivalent, hurtServer(), needs a ServerLevel reference (not just any
        // Level) as of this version, hence the instanceof check.
        if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
            for (LivingEntity livingEntity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox(),
                    candidate -> this.getOwner() != candidate)) {
                if (!this.hitEntities.contains(livingEntity)) {
                    DamageSource damageSource = this.damageSources().thrown(this, this.getOwner());
                    livingEntity.hurtServer(serverLevel, damageSource, 12.0f);
                    for (MobEffectInstance effect : this.effectInstance) {
                        livingEntity.addEffect(new MobEffectInstance(effect));
                    }
                    this.hitEntities.add(livingEntity);
                }
            }
        }
    }

    // Source: getHitSound() returning ArsenalSounds.ENTITY_BLOOD_SCYTHE_HIT. Mojmap's
    // AbstractArrow hook for this is getDefaultHitGroundSoundEvent().
    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return EntropySounds.ENTITY_BLOOD_SCYTHE_HIT;
    }

    // Source: hasNoGravity() - renamed.
    @Override
    public boolean isNoGravity() {
        return true;
    }

    // Source: onEntityHit() - renamed, still intentionally empty (damage handled
    // manually above instead).
    @Override
    protected void onHitEntity(EntityHitResult result) {
    }
}