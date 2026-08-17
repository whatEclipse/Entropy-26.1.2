package net.entropy.client.gui;

import com.google.common.collect.Lists;
import net.entropy.Entropy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WritableBookContent;
import net.entropy.client.gui.ContractPageButton;

import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class OpenContractScreen extends Screen {
    private static final Identifier BACKGROUND =
            Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "textures/gui/contract_background.png");
    private static final Component TITLE = Component.literal("Contract");
    private static final Component SIGN_BOOK_LABEL = Component.translatable("book.signButton");

    private final Player owner;
    private final ItemStack book;
    private final ContractSignScreen signScreen;
    private int currentPage;
    private final List<String> pages = Lists.newArrayList();
    private ContractPageButton forwardButton;
    private ContractPageButton backButton;
    private final InteractionHand hand;
    private Component numberOfPages;
    private MultiLineEditBox page;

    public OpenContractScreen(Player owner, ItemStack book, InteractionHand hand, WritableBookContent content) {
        super(TITLE);
        this.numberOfPages = CommonComponents.EMPTY;
        this.owner = owner;
        this.book = book;
        this.hand = hand;
        Stream<String> stream = content.getPages(Minecraft.getInstance().isTextFilteringEnabled());
        List<String> pagesList = this.pages;
        Objects.requireNonNull(pagesList);
        stream.forEach(pagesList::add);
        if (this.pages.isEmpty()) {
            this.pages.add("");
        }
        this.signScreen = new ContractSignScreen(this, owner, hand, this.pages);
    }

    private int getNumPages() {
        return this.pages.size();
    }

    @Override
    protected void init() {
        int left = this.backgroundLeft();
        int top = this.backgroundTop();

        this.page = MultiLineEditBox.builder()
                .setShowDecorations(false)
                .setTextColor(-16777216)
                .setCursorColor(-16777216)
                .setShowBackground(false)
                .setTextShadow(false)
                .setX((this.width - 114) / 2 - 8)
                .setY(28)
                .build(this.font, 122, 134, CommonComponents.EMPTY);
        this.page.setCharacterLimit(1024);
        this.page.setLineLimit(126 / 9);
        this.page.setValueListener(value -> this.pages.set(this.currentPage, value));
        this.addRenderableWidget(this.page);
        this.updatePageContent();
        this.numberOfPages = this.getPageNumberMessage();

        this.backButton = this.addRenderableWidget(new ContractPageButton(left + 43, top + 157, false, button -> this.pageBack(), true));
        this.forwardButton = this.addRenderableWidget(new ContractPageButton(left + 116, top + 157, true, button -> this.pageForward(), true));

        this.addRenderableWidget(Button.builder(SIGN_BOOK_LABEL, button -> this.minecraft.setScreen(this.signScreen))
                .pos(this.width / 2 - 98 - 2, this.menuControlsTop()).width(98).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> {
            this.minecraft.setScreen(null);
            this.saveChanges();
        }).pos(this.width / 2 + 2, this.menuControlsTop()).width(98).build());

        this.updateButtonVisibility();
    }

    private int backgroundLeft() { return (this.width - 192) / 2; }
    private int backgroundTop() { return 2; }
    private int menuControlsTop() { return this.backgroundTop() + 192 + 2; }

    @Override
    protected void setInitialFocus() {
        this.setInitialFocus(this.page);
    }

    @Override
    public Component getNarrationMessage() {
        return CommonComponents.joinForNarration(super.getNarrationMessage(), this.getPageNumberMessage());
    }

    private Component getPageNumberMessage() {
        return Component.translatable("book.pageIndicator", this.currentPage + 1, this.getNumPages())
                .withColor(-16777216).withoutShadow();
    }

    private void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
            this.updatePageContent();
        }
        this.updateButtonVisibility();
    }

    private void pageForward() {
        if (this.currentPage < this.getNumPages() - 1) {
            ++this.currentPage;
        } else {
            this.appendPageToBook();
            if (this.currentPage < this.getNumPages() - 1) {
                ++this.currentPage;
            }
        }
        this.updatePageContent();
        this.updateButtonVisibility();
    }

    private void updatePageContent() {
        this.page.setValue(this.pages.get(this.currentPage), true);
        this.numberOfPages = this.getPageNumberMessage();
    }

    private void updateButtonVisibility() {
        this.backButton.visible = this.currentPage > 0;
    }

    private void eraseEmptyTrailingPages() {
        ListIterator<String> it = this.pages.listIterator(this.pages.size());
        while (it.hasPrevious() && it.previous().isEmpty()) {
            it.remove();
        }
    }

    private void saveChanges() {
        this.eraseEmptyTrailingPages();
        this.updateLocalCopy();
        int slot = this.hand == InteractionHand.MAIN_HAND ? this.owner.getInventory().getSelectedSlot() : 40;
        this.minecraft.getConnection().send(new ServerboundEditBookPacket(slot, this.pages, Optional.empty()));
    }

    private void updateLocalCopy() {
        this.book.set(DataComponents.WRITABLE_BOOK_CONTENT,
                new WritableBookContent(this.pages.stream().map(Filterable::passThrough).toList()));
    }

    private void appendPageToBook() {
        if (this.getNumPages() < 100) {
            this.pages.add("");
        }
    }

    @Override
    public boolean isInGameUi() {
        return true;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        switch (event.key()) {
            case 266: this.backButton.onPress(event); return true;
            case 267: this.forwardButton.onPress(event); return true;
            default: return super.keyPressed(event);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        this.visitText(graphics.textRenderer());
    }

    private void visitText(ActiveTextCollector collector) {
        collector.accept(TextAlignment.RIGHT, backgroundLeft() + 148, backgroundTop() + 16, this.numberOfPages);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, this.backgroundLeft(), this.backgroundTop(),
                0.0F, 0.0F, 192, 192, 256, 256);
    }
}