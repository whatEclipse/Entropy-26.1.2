package net.entropy.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;

// The blood spew has no visible model or item - it's purely the BLOOD_BUBBLE particle
// trail spawned every tick in BloodScytheEntity#tick(). NoopRenderer draws nothing, but
// registering it is still required: without ANY renderer registered for an entity type,
// EntityRenderDispatcher NPEs when it tries to look one up (this is the exact crash from
// your original log).
public class BloodScytheRenderer extends NoopRenderer<BloodScytheEntity> {
    public BloodScytheRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
}