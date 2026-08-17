package net.entropy.client.gui;

import net.entropy.Entropy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.item.component.WrittenBookContent;

import java.util.Collections;
import java.util.List;

public class ClosedContractScreen extends Screen {
    private static final Identifier BACKGROUND =
            Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "textures/gui/contract_background.png");
    private static final Component TITLE = Component.literal("Contract");
    private static final Style PAGE_TEXT_STYLE = Style.EMPTY.withoutShadow().withColor(-16777216);

    private final List<String> pages;
    private int currentPage;
    private List<FormattedCharSequence> cachedPageComponents = Collections.emptyList();
    private int cachedPage = -1;
    private Component pageMsg = CommonComponents.EMPTY;
    private ContractPageButton forwardButton;
    private ContractPageButton backButton;

    public ClosedContractScreen(ItemStack contract) {
        super(TITLE);
        WritableBookContent content = contract.getOrDefault(DataComponents.WRITABLE_BOOK_CONTENT, WritableBookContent.EMPTY);
        this.pages = content.getPages(Minecraft.getInstance().isTextFilteringEnabled()).toList();
    }

    private int getNumPages() {
        return this.pages.size();
    }
    private String getPage(int page) {
        return page >= 0 && page < getNumPages() ? this.pages.get(page) : "";
    }

    @Override
    protected void init() {
        int left = backgroundLeft();
        int top = backgroundTop();

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose())
                .pos((this.width - 200) / 2, menuControlsTop()).width(200).build());

        this.forwardButton = this.addRenderableWidget(new ContractPageButton(left + 116, top + 157, true, button -> this.pageForward(), true));
        this.backButton = this.addRenderableWidget(new ContractPageButton(left + 43, top + 157, false, button -> this.pageBack(), true));

        this.updateButtonVisibility();
    }

    private int backgroundLeft() { return (this.width - 192) / 2; }
    private int backgroundTop() { return 2; }
    private int menuControlsTop() { return backgroundTop() + 192 + 2; }

    private Component getPageNumberMessage() {
        return Component.translatable("book.pageIndicator", this.currentPage + 1, Math.max(getNumPages(), 1))
                .withStyle(PAGE_TEXT_STYLE);
    }

    private void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
        }
        this.updateButtonVisibility();
    }

    private void pageForward() {
        if (this.currentPage < getNumPages() - 1) {
            ++this.currentPage;
        }
        this.updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        this.forwardButton.visible = this.currentPage < getNumPages() - 1;
        this.backButton.visible = this.currentPage > 0;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (super.keyPressed(event)) return true;
        switch (event.key()) {
            case 266: this.backButton.onPress(event); return true;
            case 267: this.forwardButton.onPress(event); return true;
            default: return false;
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        this.visitText(graphics.textRenderer());
    }

    private void visitText(ActiveTextCollector collector) {
        if (this.cachedPage != this.currentPage) {
            FormattedText pageText = ComponentUtils.mergeStyles(Component.literal(getPage(this.currentPage)), PAGE_TEXT_STYLE);
            this.cachedPageComponents = this.font.split(pageText, 114);
            this.pageMsg = getPageNumberMessage();
            this.cachedPage = this.currentPage;
        }

        int left = backgroundLeft();
        int top = backgroundTop();
        collector.accept(TextAlignment.RIGHT, left + 148, top + 16, this.pageMsg);

        int shownLines = Math.min(128 / 9, this.cachedPageComponents.size());
        for (int i = 0; i < shownLines; ++i) {
            FormattedCharSequence line = this.cachedPageComponents.get(i);
            collector.accept(left + 36, top + 30 + i * 9, line);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, backgroundLeft(), backgroundTop(),
                0.0F, 0.0F, 192, 192, 256, 256);
    }

    @Override
    public boolean isInGameUi() {
        return true;
    }
}