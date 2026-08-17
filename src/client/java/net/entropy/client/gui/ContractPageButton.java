package net.entropy.client.gui;

import net.entropy.Entropy;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;

public class ContractPageButton extends Button {
    private static final Identifier PAGE_FORWARD_HIGHLIGHTED_SPRITE =
            Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "textures/gui/sprites/page_forward_highlighted.png");
    private static final Identifier PAGE_FORWARD_SPRITE =
            Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "textures/gui/sprites/page_forward.png");
    private static final Identifier PAGE_BACKWARD_HIGHLIGHTED_SPRITE =
            Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "textures/gui/sprites/page_backward_highlighted.png");
    private static final Identifier PAGE_BACKWARD_SPRITE =
            Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "textures/gui/sprites/page_backward.png");
    private static final Component PAGE_BUTTON_NEXT = Component.translatable("book.page_button.next");
    private static final Component PAGE_BUTTON_PREVIOUS = Component.translatable("book.page_button.previous");

    private final boolean isForward;
    private final boolean playTurnSound;

    public ContractPageButton(int x, int y, boolean isForward, Button.OnPress onPress, boolean playTurnSound) {
        super(x, y, 23, 13, isForward ? PAGE_BUTTON_NEXT : PAGE_BUTTON_PREVIOUS, onPress, DEFAULT_NARRATION);
        this.isForward = isForward;
        this.playTurnSound = playTurnSound;
    }

    @Override
    public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        Identifier texture;
        if (this.isForward) {
            texture = this.isHoveredOrFocused() ? PAGE_FORWARD_HIGHLIGHTED_SPRITE : PAGE_FORWARD_SPRITE;
        } else {
            texture = this.isHoveredOrFocused() ? PAGE_BACKWARD_HIGHLIGHTED_SPRITE : PAGE_BACKWARD_SPRITE;
        }
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, this.getX(), this.getY(),
                0.0F, 0.0F, 23, 13, 23, 13);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        if (this.playTurnSound) {
            soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }
    }

    @Override
    public boolean shouldTakeFocusAfterInteraction() {
        return false;
    }
}