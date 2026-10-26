package net.entropy.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.entropy.entity.projectile.ThrownAtlanTridentEntity;
import net.entropy.registry.EntropyItems;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ThrownAtlanTridentRenderer extends EntityRenderer<ThrownAtlanTridentEntity, ThrownAtlanTridentRenderState> {


    private static final float ROTATION_OFFSET_Y = -90.0F;
    private static final float ROTATION_OFFSET_X = -45.0F;
    private static final float ROTATION_OFFSET_Z = 0.0F;
    private static final float SCALE = 1.5F;

    private final ItemModelResolver itemModelResolver;

    public ThrownAtlanTridentRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemModelResolver = context.getItemModelResolver();
    }

    private static ItemStack renderStack() {
        return new ItemStack(EntropyItems.ATLAN_TRIDENT);
    }

    @Override
    public ThrownAtlanTridentRenderState createRenderState() {
        return new ThrownAtlanTridentRenderState();
    }

    @Override
    public void extractRenderState(ThrownAtlanTridentEntity entity, ThrownAtlanTridentRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.yRot = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        state.xRot = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());

        this.itemModelResolver.updateForTopItem(state.item, renderStack(), ItemDisplayContext.NONE, entity.level(), entity, entity.getId());
    }

    @Override
    public void submit(ThrownAtlanTridentRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot + ROTATION_OFFSET_Y));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot + ROTATION_OFFSET_X));
        if (ROTATION_OFFSET_Z != 0.0F) {
            poseStack.mulPose(Axis.XP.rotationDegrees(ROTATION_OFFSET_Z));
        }
        if (SCALE != 1.0F) {
            poseStack.scale(SCALE, SCALE, 1.0F);
        }
        poseStack.translate(0.0D, -0.5D, -0.0D);

        state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}