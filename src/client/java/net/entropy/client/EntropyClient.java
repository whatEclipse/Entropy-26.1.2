package net.entropy;

import net.entropy.entity.BloodScytheRenderer;
import net.entropy.entity.EntropyEntities;
import net.entropy.particles.EntropyParticlesClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class EntropyClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntropyParticlesClient.registerFactories();

		EntityRendererRegistry.register(EntropyEntities.BLOOD_SCYTHE, BloodScytheRenderer::new);
	}
}