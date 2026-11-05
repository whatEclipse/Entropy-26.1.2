package net.entropy.registry;

import java.util.function.Consumer;
import java.util.function.Function;
import net.entropy.Entropy;
import net.entropy.datagen.EntropyJukeboxSongs;
import net.entropy.foods.EntropyFoods;
import net.entropy.items.contracts.ClosedContractItem;
import net.entropy.items.contracts.OpenContractItem;
import net.entropy.items.weapons.AtlanTridentItem;
import net.entropy.items.weapons.ScytheItem;
import net.entropy.items.weapons.SharkToothCleaverItem;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.level.block.Block;

public class EntropyItems {

    static ResourceKey<Item> sackItemKey = ResourceKey.create(Registries.ITEM,
            Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "sack"));

    public static final Item SCYTHE = registerItem("scythe", properties -> new ScytheItem(properties));
    public static final Item ATLAN_TRIDENT = registerItem("atlan_trident", properties -> new AtlanTridentItem(properties));
    public static final Item SHARK_TOOTH_CLEAVER = registerItem("shark_tooth_cleaver", properties -> new SharkToothCleaverItem(properties)
    {
        @Override
        public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
            builder.accept(Component.translatable("tooltip.entropy.not_implemented"));
            super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        }
    });


    public static final Item OPEN_CONTRACT = Registry.register(
            BuiltInRegistries.ITEM,
            Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "open_contract"),
            new OpenContractItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "open_contract")))
                    .component(DataComponents.WRITABLE_BOOK_CONTENT, WritableBookContent.EMPTY))
    );
    public static final Item CLOSED_CONTRACT = registerItem("closed_contract", ClosedContractItem::new);

    public static final Item BUTTER = registerItem("butter", properties -> new Item(properties
            .food(EntropyFoods.BUTTER, EntropyFoods.BUTTER_CONSUMABLE)));
    public static final Item BUTTERED_BREAD = registerItem("buttered_bread", properties -> new Item(properties
            .food(EntropyFoods.BUTTERED_BREAD, EntropyFoods.BUTTERED_BREAD_CONSUMABLE)));
    public static final Item SALT = registerItem("salt", properties -> new Item(properties));
    public static final Item SCYTHE_UPGRADE_TEMPLATE = registerItem("scythe_upgrade_template", properties -> new Item(properties));

    public static final Item MUSIC_DISC_DELIRIO = registerItem("music_disc_delirio", properties
            -> new Item(properties.jukeboxPlayable(EntropyJukeboxSongs.DELIRIO_KEY).stacksTo(1).rarity(Rarity.RARE)));
    //public static final Item MUSIC_DISC_I_WILL_NEVER_BE_ON_YOUR_SIDE_AGAIN = registerItem("music_disc_i_will_never_be_on_your_side_again", properties
    //       -> new Item(properties.jukeboxPlayable(EntropyJukeboxSongs.I_WILL_NEVER_BE_ON_YOUR_SIDE_AGAIN_KEY).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item MUSIC_DISC_PERFECT_FUNERAL = registerItem("music_disc_perfect_funeral", properties
         -> new Item(properties.jukeboxPlayable(EntropyJukeboxSongs.PERFECT_FUNERAL_KEY).stacksTo(1).rarity(Rarity.EPIC)));

    public static final Item AMETHYST_BRICKS = registerBlockItem("amethyst_bricks", EntropyBlocks.AMETHYST_BRICKS);
    public static final Item AMETHYST_BRICK_STAIRS = registerBlockItem("amethyst_brick_stairs", EntropyBlocks.AMETHYST_BRICK_STAIRS);
    public static final Item AMETHYST_BRICK_SLAB = registerBlockItem("amethyst_brick_slab", EntropyBlocks.AMETHYST_BRICK_SLAB);
    public static final Item AMETHYST_BRICK_WALL = registerBlockItem("amethyst_brick_wall", EntropyBlocks.AMETHYST_BRICK_WALL);
    public static final Item AMETHYST_PILLAR = registerBlockItem("amethyst_pillar", EntropyBlocks.AMETHYST_PILLAR);
    public static final Item CHISELED_AMETHYST = registerBlockItem("chiseled_amethyst", EntropyBlocks.CHISELED_AMETHYST);

    public static final Item CAST_IRON_SHEET = registerBlockItem("cast_iron_sheet", EntropyBlocks.CAST_IRON_SHEET);
    public static final Item CAST_IRON_SHEET_STAIRS = registerBlockItem("cast_iron_sheet_stairs", EntropyBlocks.CAST_IRON_SHEET_STAIRS);
    public static final Item CAST_IRON_SHEET_SLAB = registerBlockItem("cast_iron_sheet_slab", EntropyBlocks.CAST_IRON_SHEET_SLAB);
    public static final Item CAST_IRON_SHEET_WALL = registerBlockItem("cast_iron_sheet_wall", EntropyBlocks.CAST_IRON_SHEET_WALL);
    public static final Item CAST_IRON_HULL = registerBlockItem("cast_iron_hull", EntropyBlocks.CAST_IRON_HULL);
    public static final Item CAST_IRON_SUPPORT_BEAM = registerBlockItem("cast_iron_support_beam", EntropyBlocks.CAST_IRON_SUPPORT_BEAM);
    public static final Item CAST_IRON_CRATE = registerBlockItem("cast_iron_crate", EntropyBlocks.CAST_IRON_CRATE);
    public static final Item CAST_IRON_GRATE = registerBlockItem("cast_iron_grate", EntropyBlocks.CAST_IRON_GRATE);
    public static final Item CAST_IRON_GRATE_STAIRS = registerBlockItem("cast_iron_grate_stairs", EntropyBlocks.CAST_IRON_GRATE_STAIRS);
    public static final Item CAST_IRON_DOOR = registerDoubleBlockItem("cast_iron_door", EntropyBlocks.CAST_IRON_DOOR);
    public static final Item CAST_IRON_PORTHOLE_DOOR = registerDoubleBlockItem("cast_iron_porthole_door", EntropyBlocks.CAST_IRON_PORTHOLE_DOOR);
    public static final Item CAST_IRON_LANTERN = registerBlockItem("cast_iron_lantern", EntropyBlocks.CAST_IRON_LANTERN);
    public static final Item VERDIGRIS_HULL = registerBlockItem("verdigris_hull", EntropyBlocks.VERDIGRIS_HULL);
    public static final Item RAT_MAID_PLUSHIE = registerBlockItem("rat_maid_plushie", EntropyBlocks.RAT_MAID_PLUSHIE);
    public static final Item MOSS_LAYER = registerBlockItem("moss_layer", EntropyBlocks.MOSS_LAYER);
    public static final Item SNOW_LAYER = registerBlockItem("snow_layer", EntropyBlocks.SNOW_LAYER);
    public static final Item STICKSHIFT = registerBlockItem("stickshift", EntropyBlocks.STICKSHIFT);
    public static final Item GIANT_ACORN_BLOCK = registerBlockItem("giant_acorn_block", EntropyBlocks.GIANT_ACORN_BLOCK);
    public static final Item IRON_SCAFFOLDING = registerBlockItem("iron_scaffolding", EntropyBlocks.IRON_SCAFFOLDING);
    public static final Item STEEL_BAR = registerBlockItem("steel_bar", EntropyBlocks.STEEL_BAR);
    public static final Item BRAZIER = registerBlockItem("brazier", EntropyBlocks.BRAZIER);
    public static final Item SOUL_BRAZIER = registerBlockItem("soul_brazier", EntropyBlocks.SOUL_BRAZIER);
    public static final Item CHOCOLATE_EGG = registerBlockItem("chocolate_egg", EntropyBlocks.CHOCOLATE_EGG);
    public static final Item CHISELED_CHOCOLATE_EGG = registerBlockItem("chiseled_chocolate_egg", EntropyBlocks.CHISELED_CHOCOLATE_EGG);
    public static final Item CHOCOLATE_FROG = registerBlockItem("chocolate_frog", EntropyBlocks.CHOCOLATE_FROG);
    public static final Item CHOCOLATE_RAT = registerBlockItem("chocolate_rat", EntropyBlocks.CHOCOLATE_RAT);
    public static final Item CHOCOLATE_RAVEN = registerBlockItem("chocolate_raven", EntropyBlocks.CHOCOLATE_RAVEN);
    public static final Item CHOCOLATE_SQUIRREL = registerBlockItem("chocolate_squirrel", EntropyBlocks.CHOCOLATE_SQUIRREL);
    public static final Item CHOCOLATE_CREWMATE = registerBlockItem("chocolate_crewmate", EntropyBlocks.CHOCOLATE_CREWMATE);;
    public static final Item POLISHED_CALCITE = registerBlockItem("polished_calcite", EntropyBlocks.POLISHED_CALCITE);
    public static final Item POLISHED_CALCITE_STAIRS = registerBlockItem("polished_calcite_stairs", EntropyBlocks.POLISHED_CALCITE_STAIRS);
    public static final Item POLISHED_CALCITE_SLAB = registerBlockItem("polished_calcite_slab", EntropyBlocks.POLISHED_CALCITE_SLAB);
    public static final Item POLISHED_CALCITE_WALL = registerBlockItem("polished_calcite_wall", EntropyBlocks.POLISHED_CALCITE_WALL);
    public static final Item CALCITE_BRICKS = registerBlockItem("calcite_bricks", EntropyBlocks.CALCITE_BRICKS);
    public static final Item CALCITE_BRICK_STAIRS = registerBlockItem("calcite_brick_stairs", EntropyBlocks.CALCITE_BRICK_STAIRS);
    public static final Item CALCITE_BRICK_SLAB = registerBlockItem("calcite_brick_slab", EntropyBlocks.CALCITE_BRICK_SLAB);
    public static final Item CALCITE_BRICK_WALL = registerBlockItem("calcite_brick_wall", EntropyBlocks.CALCITE_BRICK_WALL);
    public static final Item POLISHED_TUFF = registerBlockItem("polished_tuff", EntropyBlocks.POLISHED_TUFF);
    public static final Item POLISHED_TUFF_STAIRS = registerBlockItem("polished_tuff_stairs", EntropyBlocks.POLISHED_TUFF_STAIRS);
    public static final Item POLISHED_TUFF_SLAB = registerBlockItem("polished_tuff_slab", EntropyBlocks.POLISHED_TUFF_SLAB);
    public static final Item POLISHED_TUFF_WALL = registerBlockItem("polished_tuff_wall", EntropyBlocks.POLISHED_TUFF_WALL);
    public static final Item TUFF_BRICKS = registerBlockItem("tuff_bricks", EntropyBlocks.TUFF_BRICKS);
    public static final Item TUFF_BRICK_STAIRS = registerBlockItem("tuff_brick_stairs", EntropyBlocks.TUFF_BRICK_STAIRS);
    public static final Item TUFF_BRICK_SLAB = registerBlockItem("tuff_brick_slab", EntropyBlocks.TUFF_BRICK_SLAB);
    public static final Item TUFF_BRICK_WALL = registerBlockItem("tuff_brick_wall", EntropyBlocks.TUFF_BRICK_WALL);
    public static final Item SMOOTH_BASALT_STAIRS = registerBlockItem("smooth_basalt_stairs", EntropyBlocks.SMOOTH_BASALT_STAIRS);
    public static final Item SMOOTH_BASALT_SLAB = registerBlockItem("smooth_basalt_slab", EntropyBlocks.SMOOTH_BASALT_SLAB);
    public static final Item SMOOTH_BASALT_WALL = registerBlockItem("smooth_basalt_wall", EntropyBlocks.SMOOTH_BASALT_WALL);
    public static final Item SMOOTH_SANDSTONE_WALL = registerBlockItem("smooth_sandstone_wall", EntropyBlocks.SMOOTH_SANDSTONE_WALL);
    public static final Item SMOOTH_RED_SANDSTONE_WALL = registerBlockItem("smooth_red_sandstone_wall", EntropyBlocks.SMOOTH_RED_SANDSTONE_WALL);
    public static final Item POLISHED_ANDESITE_WALL = registerBlockItem("polished_andesite_wall", EntropyBlocks.POLISHED_ANDESITE_WALL);
    public static final Item POLISHED_DIORITE_WALL = registerBlockItem("polished_diorite_wall", EntropyBlocks.POLISHED_DIORITE_WALL);
    public static final Item POLISHED_GRANITE_WALL = registerBlockItem("polished_granite_wall", EntropyBlocks.POLISHED_GRANITE_WALL);
    public static final Item QUARTZ_WALL = registerBlockItem("quartz_wall", EntropyBlocks.QUARTZ_WALL);
    public static final Item SMOOTH_QUARTZ_WALL = registerBlockItem("smooth_quartz_wall", EntropyBlocks.SMOOTH_QUARTZ_WALL);
    public static final Item MUG = registerBlockItem("mug", EntropyBlocks.MUG);
    public static final Item STRIPPED_OAK_BRANCH = registerBlockItem("stripped_oak_branch", EntropyBlocks.STRIPPED_OAK_BRANCH);
    public static final Item STRIPPED_SPRUCE_BRANCH = registerBlockItem("stripped_spruce_branch", EntropyBlocks.STRIPPED_SPRUCE_BRANCH);
    public static final Item STRIPPED_BIRCH_BRANCH = registerBlockItem("stripped_birch_branch", EntropyBlocks.STRIPPED_BIRCH_BRANCH);
    public static final Item STRIPPED_JUNGLE_BRANCH = registerBlockItem("stripped_jungle_branch", EntropyBlocks.STRIPPED_JUNGLE_BRANCH);
    public static final Item STRIPPED_ACACIA_BRANCH = registerBlockItem("stripped_acacia_branch", EntropyBlocks.STRIPPED_ACACIA_BRANCH);
    public static final Item STRIPPED_DARK_OAK_BRANCH = registerBlockItem("stripped_dark_oak_branch", EntropyBlocks.STRIPPED_DARK_OAK_BRANCH);
    public static final Item STRIPPED_CRIMSON_BRANCH = registerBlockItem("stripped_crimson_branch", EntropyBlocks.STRIPPED_CRIMSON_BRANCH);
    public static final Item STRIPPED_WARPED_BRANCH = registerBlockItem("stripped_warped_branch", EntropyBlocks.STRIPPED_WARPED_BRANCH);
    public static final Item STRIPPED_MANGROVE_BRANCH = registerBlockItem("stripped_mangrove_branch", EntropyBlocks.STRIPPED_MANGROVE_BRANCH);
    public static final Item OAK_BRANCH = registerBlockItem("oak_branch", EntropyBlocks.OAK_BRANCH);
    public static final Item SPRUCE_BRANCH = registerBlockItem("spruce_branch", EntropyBlocks.SPRUCE_BRANCH);
    public static final Item BIRCH_BRANCH = registerBlockItem("birch_branch", EntropyBlocks.BIRCH_BRANCH);
    public static final Item JUNGLE_BRANCH = registerBlockItem("jungle_branch", EntropyBlocks.JUNGLE_BRANCH);
    public static final Item ACACIA_BRANCH = registerBlockItem("acacia_branch", EntropyBlocks.ACACIA_BRANCH);
    public static final Item DARK_OAK_BRANCH = registerBlockItem("dark_oak_branch", EntropyBlocks.DARK_OAK_BRANCH);
    public static final Item CRIMSON_BRANCH = registerBlockItem("crimson_branch", EntropyBlocks.CRIMSON_BRANCH);
    public static final Item WARPED_BRANCH = registerBlockItem("warped_branch", EntropyBlocks.WARPED_BRANCH);
    public static final Item MANGROVE_BRANCH = registerBlockItem("mangrove_branch", EntropyBlocks.MANGROVE_BRANCH);
    public static final Item AQUARIUM_GLASS = registerBlockItem("aquarium_glass", EntropyBlocks.AQUARIUM_GLASS);
    public static final Item EQUIVALENCE_DETECTOR = registerBlockItem("equivalence_detector", EntropyBlocks.EQUIVALENCE_DETECTOR);

    private static Item registerItem(String name, Function<Item.Properties, Item> properties) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(Entropy.MOD_ID, name),
                properties.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM,
                        Identifier.fromNamespaceAndPath(Entropy.MOD_ID, name)))));
    }
    private static Item registerBlockItem(String name, Block block) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(Entropy.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ITEM, key, new BlockItem(block, new Item.Properties().setId(key)));
    }

    private static Item registerDoubleBlockItem(String name, Block block) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(Entropy.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ITEM, key, new DoubleHighBlockItem(block, new Item.Properties().setId(key)));
    }

    public static void initialize() {
    }
}
