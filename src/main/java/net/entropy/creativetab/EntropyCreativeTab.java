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
                output.accept(EntropyItems.CAST_IRON_SHEET);
                output.accept(EntropyItems.CAST_IRON_SHEET_SLAB);
                output.accept(EntropyItems.CAST_IRON_SHEET_STAIRS);
                output.accept(EntropyItems.CAST_IRON_SHEET_WALL);
                output.accept(EntropyItems.CAST_IRON_HULL);
                output.accept(EntropyItems.VERDIGRIS_HULL);
                output.accept(EntropyItems.CAST_IRON_SUPPORT_BEAM);
                output.accept(EntropyItems.CAST_IRON_CRATE);
                output.accept(EntropyItems.CAST_IRON_GRATE);
                output.accept(EntropyItems.CAST_IRON_GRATE_STAIRS);
                output.accept(EntropyItems.CAST_IRON_DOOR);
                output.accept(EntropyItems.CAST_IRON_PORTHOLE_DOOR);
                output.accept(EntropyItems.CAST_IRON_LANTERN);
                output.accept(EntropyItems.RAT_MAID_PLUSHIE);
                output.accept(EntropyItems.MOSS_LAYER);
                output.accept(EntropyItems.SNOW_LAYER);
                output.accept(EntropyItems.STICKSHIFT);
                output.accept(EntropyItems.GIANT_ACORN_BLOCK);
                output.accept(EntropyItems.IRON_SCAFFOLDING);
                output.accept(EntropyItems.STEEL_BAR);
                output.accept(EntropyItems.BRAZIER);
                output.accept(EntropyItems.SOUL_BRAZIER);
                output.accept(EntropyItems.CHOCOLATE_EGG);
                output.accept(EntropyItems.CHISELED_CHOCOLATE_EGG);
                output.accept(EntropyItems.CHOCOLATE_FROG);
                output.accept(EntropyItems.CHOCOLATE_RAT);
                output.accept(EntropyItems.CHOCOLATE_RAVEN);
                output.accept(EntropyItems.CHOCOLATE_SQUIRREL);
                output.accept(EntropyItems.CHOCOLATE_CREWMATE);
                output.accept(EntropyItems.POLISHED_CALCITE);
                output.accept(EntropyItems.POLISHED_CALCITE_STAIRS);
                output.accept(EntropyItems.POLISHED_CALCITE_SLAB);
                output.accept(EntropyItems.POLISHED_CALCITE_WALL);
                output.accept(EntropyItems.CALCITE_BRICKS);
                output.accept(EntropyItems.CALCITE_BRICK_STAIRS);
                output.accept(EntropyItems.CALCITE_BRICK_SLAB);
                output.accept(EntropyItems.CALCITE_BRICK_WALL);
                output.accept(EntropyItems.POLISHED_TUFF);
                output.accept(EntropyItems.POLISHED_TUFF_STAIRS);
                output.accept(EntropyItems.POLISHED_TUFF_SLAB);
                output.accept(EntropyItems.POLISHED_TUFF_WALL);
                output.accept(EntropyItems.TUFF_BRICKS);
                output.accept(EntropyItems.TUFF_BRICK_STAIRS);
                output.accept(EntropyItems.TUFF_BRICK_SLAB);
                output.accept(EntropyItems.TUFF_BRICK_WALL);
                output.accept(EntropyItems.SMOOTH_BASALT_STAIRS);
                output.accept(EntropyItems.SMOOTH_BASALT_SLAB);
                output.accept(EntropyItems.SMOOTH_BASALT_WALL);
                output.accept(EntropyItems.SMOOTH_SANDSTONE_WALL);
                output.accept(EntropyItems.SMOOTH_RED_SANDSTONE_WALL);
                output.accept(EntropyItems.POLISHED_ANDESITE_WALL);
                output.accept(EntropyItems.POLISHED_DIORITE_WALL);
                output.accept(EntropyItems.POLISHED_GRANITE_WALL);
                output.accept(EntropyItems.QUARTZ_WALL);
                output.accept(EntropyItems.SMOOTH_QUARTZ_WALL);
                output.accept(EntropyItems.MUG);
                output.accept(EntropyItems.STRIPPED_OAK_BRANCH);
                output.accept(EntropyItems.STRIPPED_SPRUCE_BRANCH);
                output.accept(EntropyItems.STRIPPED_BIRCH_BRANCH);
                output.accept(EntropyItems.STRIPPED_JUNGLE_BRANCH);
                output.accept(EntropyItems.STRIPPED_ACACIA_BRANCH);
                output.accept(EntropyItems.STRIPPED_DARK_OAK_BRANCH);
                output.accept(EntropyItems.STRIPPED_CRIMSON_BRANCH);
                output.accept(EntropyItems.STRIPPED_WARPED_BRANCH);
                output.accept(EntropyItems.STRIPPED_MANGROVE_BRANCH);
                output.accept(EntropyItems.OAK_BRANCH);
                output.accept(EntropyItems.SPRUCE_BRANCH);
                output.accept(EntropyItems.BIRCH_BRANCH);
                output.accept(EntropyItems.JUNGLE_BRANCH);
                output.accept(EntropyItems.ACACIA_BRANCH);
                output.accept(EntropyItems.DARK_OAK_BRANCH);
                output.accept(EntropyItems.CRIMSON_BRANCH);
                output.accept(EntropyItems.WARPED_BRANCH);
                output.accept(EntropyItems.MANGROVE_BRANCH);
                output.accept(EntropyItems.AQUARIUM_GLASS);
                output.accept(EntropyItems.EQUIVALENCE_DETECTOR);
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