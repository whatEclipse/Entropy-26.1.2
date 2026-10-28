package net.entropy.creativetab;

import net.entropy.Entropy;
import net.entropy.registry.EntropyEnchantments;
import net.entropy.registry.EntropyItems;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class EntropyCreativeTab {

    public static final ResourceKey<CreativeModeTab> CREATIVE_MODE_TAB_RESOURCE_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "creative_tab")
    );
    public static final CreativeModeTab ENTROPY_CREATIVE_TAB = FabricCreativeModeTab.builder()
            .icon(()-> new ItemStack(EntropyItems.CLOSED_CONTRACT))
            .title(Component.translatable("creativeTab.entropy_mod"))
            .displayItems(((parameters, output) -> {
                output.accept(EntropyItems.AMETHYST_BRICKS);
                output.accept(EntropyItems.AMETHYST_BRICK_SLAB);
                output.accept(EntropyItems.AMETHYST_BRICK_STAIRS);
                output.accept(EntropyItems.AMETHYST_BRICK_WALL);
                output.accept(EntropyItems.AMETHYST_PILLAR);
                output.accept(EntropyItems.CHISELED_AMETHYST);
                output.accept(EntropyItems.SCYTHE_UPGRADE_TEMPLATE);
                output.accept(EntropyItems.CLOSED_CONTRACT);
                output.accept(EntropyItems.OPEN_CONTRACT);
                output.accept(EntropyItems.BUTTER);
                output.accept(EntropyItems.BUTTERED_BREAD);
                output.accept(EntropyItems.SALT);
                output.accept(EntropyItems.MUSIC_DISC_DELIRIO);
                //output.accept(EntropyItems.MUSIC_DISC_I_WILL_NEVER_BE_ON_YOUR_SIDE_AGAIN);
                output.accept(EntropyItems.MUSIC_DISC_PERFECT_FUNERAL);
                output.accept(EntropyItems.SHARK_TOOTH_CLEAVER);
                output.accept(EntropyItems.SCYTHE);
                output.accept(EntropyItems.ATLAN_TRIDENT);

                Holder<Enchantment> spewing = parameters.holders().getOrThrow(EntropyEnchantments.SPEWING);
                ItemStack spewingBook = new ItemStack(Items.ENCHANTED_BOOK);
                ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
                mutable.set(spewing, 1);
                spewingBook.set(DataComponents.STORED_ENCHANTMENTS, mutable.toImmutable());
                output.accept(spewingBook);

                Holder<Enchantment> reeling = parameters.holders().getOrThrow(EntropyEnchantments.REELING);
                ItemStack reelingBook = new ItemStack(Items.ENCHANTED_BOOK);
                ItemEnchantments.Mutable mutable1 = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
                mutable1.set(reeling, 1);
                reelingBook.set(DataComponents.STORED_ENCHANTMENTS, mutable1.toImmutable());
                output.accept(reelingBook);

            }))
            .build();




    public static void initialize(){
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CREATIVE_MODE_TAB_RESOURCE_KEY, ENTROPY_CREATIVE_TAB);
    }
}


