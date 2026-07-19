package net.entropy;

import net.entropy.entity.BloodScytheRenderer;
import net.entropy.entity.EntropyEntities;
import net.entropy.particles.EntropyParticlesClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

// Belongs in src/client/java/net/entropy/EntropyClient.java.
// This is the client-side entry point Fabric calls in addition to your main
// ModInitializer - it's where ALL client-only registration goes: particle factories,
// entity renderers, screens, key bindings, etc.
public class EntropyClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntropyParticlesClient.registerFactories();

		EntityRendererRegistry.register(EntropyEntities.BLOOD_SCYTHE, BloodScytheRenderer::new);
	}
}