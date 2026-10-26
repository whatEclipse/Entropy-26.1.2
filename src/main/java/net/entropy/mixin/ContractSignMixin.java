package net.entropy.mixin;

import net.entropy.datacomponents.ContractSigners;
import net.entropy.registry.EntropyDataComponents;
import net.entropy.items.contracts.OpenContractItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.Filterable;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.WritableBookContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ContractSignMixin {

    @Shadow
    public ServerPlayer player;

    @Inject(method = "signBook", at = @At("HEAD"), cancellable = true)
    private void entropy$onSignBook(FilteredText title, List<FilteredText> contents, int slot, CallbackInfo ci) {
        ItemStack carried = this.player.getInventory().getItem(slot);

        if (!(carried.getItem() instanceof OpenContractItem)) {
            return;
        }

        List<Filterable<String>> pages = contents.stream()
                .map(page -> this.player.isTextFilteringEnabled()
                        ? Filterable.passThrough(page.filteredOrEmpty())
                        : Filterable.from(page))
                .toList();
        carried.set(DataComponents.WRITABLE_BOOK_CONTENT, new WritableBookContent(pages));

        ContractSigners signers = carried.getOrDefault(EntropyDataComponents.CONTRACT_SIGNERS, ContractSigners.EMPTY);
        ContractSigners updated = signers.withFirstSigner(this.player.getGameProfile().name());
        carried.set(EntropyDataComponents.CONTRACT_SIGNERS, updated);
        List<Component> loreLines = List.of(
                Component.translatable("entropy.contract.signed_by", this.player.getGameProfile().name())
        );
        carried.set(DataComponents.LORE, new ItemLore(loreLines));

        this.player.getInventory().setItem(slot, carried);
        this.player.containerMenu.broadcastChanges();
        ci.cancel();
    }
}