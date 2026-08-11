package net.entropy.mixin;
/*
import net.entropy.items.EntropyItems;
import net.entropy.items.OpenContractItem;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ContractSignMixin {

    @Redirect(
            method = "signBook",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;transmuteCopy(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/ItemStack;"
            )
    )
    private ItemStack entropy$redirectContractSign(ItemStack carried, Item target) {
        if (carried.getItem() instanceof OpenContractItem) {
            return carried.transmuteCopy(EntropyItems.CLOSED_CONTRACT);
        }
        return carried.transmuteCopy(target);
    }
}

 */