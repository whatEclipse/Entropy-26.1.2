package net.entropy.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

// Ported from Arsenal's actual BloodBubbleSplatterParticle - this is a genuinely separate
// class from BloodBubbleParticle, not a reuse of it. The real difference is gravity: this
// one falls (velocityY -= 0.05 each tick) while the trail particle hovers.
public class BloodBubbleSplatterParticle extends SingleQuadParticle {
    private final SpriteSet spriteSet;

    public BloodBubbleSplatterParticle(ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteSet spriteSet) {
        super(level, x, y, z, spriteSet.first());
        this.spriteSet = spriteSet;
        this.setSpriteFromAge(spriteSet);
        // Source's scale range is 0.5-1.0, bigger than the trail particle's 0.25-0.75.
        this.scale(0.5F + this.random.nextFloat() * 0.5F);
        this.setParticleSpeed(velocityX, velocityY, velocityZ);
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(this.spriteSet);
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.getLifetime()) {
            this.remove();
            return;
        }

        // The real difference from the trail particle: this one actually falls.
        this.yd -= 0.05;
        this.move(this.xd, this.yd, this.zd);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new BloodBubbleSplatterParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
