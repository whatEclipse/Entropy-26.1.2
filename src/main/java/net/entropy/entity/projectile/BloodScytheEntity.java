package net.entropy.entity.projectile;

import com.google.common.collect.Sets;
import net.entropy.entity.EntropyEntities;
import net.entropy.particles.EntropyParticles;
import net.entropy.sounds.EntropySounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
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

    private final float particleAngle = 35.0F + this.random.nextFloat() * 20.0F;

    public BloodScytheEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public BloodScytheEntity(Level level, LivingEntity owner) {
        super(EntropyEntities.BLOOD_SCYTHE, owner, level, ItemStack.EMPTY, null);
    }

    public void addEffect(MobEffectInstance effect) {
        this.effectInstance.add(effect);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

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

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return EntropySounds.ENTITY_BLOOD_SCYTHE_HIT;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
    }
}