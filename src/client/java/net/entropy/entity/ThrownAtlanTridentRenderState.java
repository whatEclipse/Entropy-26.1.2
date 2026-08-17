package net.entropy.entity;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class ThrownAtlanTridentRenderState extends EntityRenderState {
    public final ItemStackRenderState item = new ItemStackRenderState();
    public float yRot;
    public float xRot;
}