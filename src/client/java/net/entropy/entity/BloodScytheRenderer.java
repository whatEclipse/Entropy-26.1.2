package net.entropy.entity;

import net.entropy.entity.projectile.BloodScytheEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;

public class BloodScytheRenderer extends NoopRenderer<BloodScytheEntity> {
    public BloodScytheRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
}