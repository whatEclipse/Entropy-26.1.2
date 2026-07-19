package net.entropy.particles;

import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;


public class EntropyParticlesClient {
    public static void registerFactories() {
        ParticleProviderRegistry.getInstance().register(EntropyParticles.BLOOD_BUBBLE, BloodBubbleParticle.Factory::new);
        ParticleProviderRegistry.getInstance().register(EntropyParticles.BLOOD_BUBBLE_SPLATTER, BloodBubbleSplatterParticle.Factory::new);
    }
}