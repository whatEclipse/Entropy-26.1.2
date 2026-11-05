package net.entropy.registry;

import java.util.function.Function;
import net.entropy.Entropy;
import net.entropy.blocks.CastIronCrateBlock;
import net.entropy.blocks.RatMaidPlushieBlock;
import net.entropy.blocks.AquariumGlassBlock;
import net.entropy.blocks.BranchBlock;
import net.entropy.blocks.BrazierBlock;
import net.entropy.blocks.ChocolateBlock;
import net.entropy.blocks.EquivalenceDetectorBlock;
import net.entropy.blocks.GiantAcornBlock;
import net.entropy.blocks.IronScaffoldingBlock;
import net.entropy.blocks.LayerBlock;
import net.entropy.blocks.SoulBrazierBlock;
import net.entropy.blocks.SteelBarBlock;
import net.entropy.blocks.StickshiftBlock;
import net.entropy.blocks.mug.MugBlock;
import net.entropy.blocks.mug.HoneyMugBlock;
import net.entropy.blocks.mug.LavaMugBlock;
import net.entropy.blocks.mug.MilkMugBlock;
import net.entropy.blocks.mug.PotionMugBlock;
import net.entropy.blocks.mug.PowderSnowMugBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class EntropyBlocks {

    public static final Block AMETHYST_BRICKS = registerBlock("amethyst_bricks",
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .requiresCorrectToolForDrops()
                    .strength(4.0F, 6.0F)
                    .sound(SoundType.AMETHYST));

    public static final Block AMETHYST_BRICK_STAIRS = registerBlock("amethyst_brick_stairs",
            properties -> new StairBlock(AMETHYST_BRICKS.defaultBlockState(), properties) {},
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .requiresCorrectToolForDrops()
                    .strength(4.0F, 6.0F)
                    .sound(SoundType.AMETHYST));

    public static final Block AMETHYST_BRICK_SLAB = registerBlock("amethyst_brick_slab",
            SlabBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .requiresCorrectToolForDrops()
                    .strength(4.0F, 6.0F)
                    .sound(SoundType.AMETHYST));

    public static final Block AMETHYST_BRICK_WALL = registerBlock("amethyst_brick_wall",
            WallBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .requiresCorrectToolForDrops()
                    .strength(4.0F, 6.0F)
                    .sound(SoundType.AMETHYST));

    public static final Block AMETHYST_PILLAR = registerBlock("amethyst_pillar",
            RotatedPillarBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .requiresCorrectToolForDrops()
                    .strength(4.0F, 6.0F)
                    .sound(SoundType.AMETHYST)
                    .pushReaction(PushReaction.NORMAL));

    public static final Block CHISELED_AMETHYST = registerBlock("chiseled_amethyst",
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .requiresCorrectToolForDrops()
                    .strength(4.0F, 6.0F)
                    .sound(SoundType.AMETHYST));

    public static final Block CAST_IRON_SHEET = registerBlock("cast_iron_sheet",
            Block::new,
            castIron());

    public static final Block CAST_IRON_SHEET_STAIRS = registerBlock("cast_iron_sheet_stairs",
            properties -> new StairBlock(CAST_IRON_SHEET.defaultBlockState(), properties) {},
            castIron());

    public static final Block CAST_IRON_SHEET_SLAB = registerBlock("cast_iron_sheet_slab",
            SlabBlock::new,
            castIron());

    public static final Block CAST_IRON_SHEET_WALL = registerBlock("cast_iron_sheet_wall",
            WallBlock::new,
            castIron());

    public static final Block CAST_IRON_HULL = registerBlock("cast_iron_hull",
            RotatedPillarBlock::new,
            castIron());

    public static final Block CAST_IRON_SUPPORT_BEAM = registerBlock("cast_iron_support_beam",
            Block::new,
            castIron().noOcclusion());

    public static final Block CAST_IRON_CRATE = registerBlock("cast_iron_crate",
            CastIronCrateBlock::new,
            castIron());

    public static final Block CAST_IRON_GRATE = registerBlock("cast_iron_grate",
            properties -> new TrapDoorBlock(BlockSetType.COPPER, properties) {},
            castIron().noOcclusion());

    public static final Block CAST_IRON_GRATE_STAIRS = registerBlock("cast_iron_grate_stairs",
            properties -> new StairBlock(CAST_IRON_SHEET.defaultBlockState(), properties) {},
            castIron().noOcclusion());

    public static final Block CAST_IRON_DOOR = registerBlock("cast_iron_door",
            properties -> new DoorBlock(BlockSetType.COPPER, properties) {},
            castIron().noOcclusion().pushReaction(PushReaction.DESTROY));

    public static final Block CAST_IRON_PORTHOLE_DOOR = registerBlock("cast_iron_porthole_door",
            properties -> new DoorBlock(BlockSetType.COPPER, properties) {},
            castIron().noOcclusion().pushReaction(PushReaction.DESTROY));

    public static final Block CAST_IRON_LANTERN = registerBlock("cast_iron_lantern",
            LanternBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .forceSolidOn()
                    .requiresCorrectToolForDrops()
                    .strength(3.5F)
                    .sound(SoundType.COPPER)
                    .lightLevel(state -> 15)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY));

    public static final Block VERDIGRIS_HULL = registerBlock("verdigris_hull",
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WARPED_STEM)
                    .requiresCorrectToolForDrops()
                    .strength(4.0F, 6.0F)
                    .sound(SoundType.COPPER));

    public static final Block RAT_MAID_PLUSHIE = registerBlock("rat_maid_plushie",
            RatMaidPlushieBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(0.8F)
                    .sound(SoundType.WOOL)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY));

    public static final Block MOSS_LAYER = registerBlock("moss_layer",
            LayerBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).sound(SoundType.MOSS_CARPET).noCollision().instabreak().replaceable().pushReaction(PushReaction.DESTROY));

    public static final Block SNOW_LAYER = registerBlock("snow_layer",
            LayerBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).sound(SoundType.POWDER_SNOW).noCollision().instabreak().replaceable().pushReaction(PushReaction.DESTROY));

    public static final Block STICKSHIFT = registerBlock("stickshift",
            StickshiftBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.LEVER));

    public static final Block GIANT_ACORN_BLOCK = registerBlock("giant_acorn_block",
            GiantAcornBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.0F).sound(SoundType.WOOD));

    public static final Block IRON_SCAFFOLDING = registerBlock("iron_scaffolding",
            IronScaffoldingBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(1.0F).noCollision().sound(SoundType.LANTERN).noOcclusion().dynamicShape());

    public static final Block STEEL_BAR = registerBlock("steel_bar",
            SteelBarBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS));

    public static final Block BRAZIER = registerBlock("brazier",
            BrazierBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN).lightLevel(BrazierBlock::getLuminance));

    public static final Block SOUL_BRAZIER = registerBlock("soul_brazier",
            SoulBrazierBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN).lightLevel(SoulBrazierBlock::getSoulLuminance));

    public static final Block CHOCOLATE_EGG = registerBlock("chocolate_egg",
            p -> new ChocolateBlock(p, 3, 9),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(0.2F, 3.0F).noOcclusion().sound(SoundType.CANDLE));

    public static final Block CHISELED_CHOCOLATE_EGG = registerBlock("chiseled_chocolate_egg",
            p -> new ChocolateBlock(p, 4, 8),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(0.2F, 3.0F).noOcclusion().sound(SoundType.CANDLE));

    public static final Block CHOCOLATE_FROG = registerBlock("chocolate_frog",
            p -> new ChocolateBlock(p, 4, 5),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(0.2F, 3.0F).noOcclusion().sound(SoundType.CANDLE));

    public static final Block CHOCOLATE_RAT = registerBlock("chocolate_rat",
            p -> new ChocolateBlock(p, 4, 6),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(0.2F, 3.0F).noOcclusion().sound(SoundType.CANDLE));

    public static final Block CHOCOLATE_RAVEN = registerBlock("chocolate_raven",
            p -> new ChocolateBlock(p, 3, 5),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(0.2F, 3.0F).noOcclusion().sound(SoundType.CANDLE));

    public static final Block CHOCOLATE_SQUIRREL = registerBlock("chocolate_squirrel",
            p -> new ChocolateBlock(p, 4, 10),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(0.2F, 3.0F).noOcclusion().sound(SoundType.CANDLE));

    public static final Block CHOCOLATE_CREWMATE = registerBlock("chocolate_crewmate",
            p -> new ChocolateBlock(p, 3, 8),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(0.2F, 3.0F).noOcclusion().sound(SoundType.CANDLE));

    public static final Block POLISHED_CALCITE = registerBlock("polished_calcite",
            Block::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE));

    public static final Block POLISHED_CALCITE_STAIRS = registerBlock("polished_calcite_stairs",
            p -> new StairBlock(POLISHED_CALCITE.defaultBlockState(), p),
            BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE));

    public static final Block POLISHED_CALCITE_SLAB = registerBlock("polished_calcite_slab",
            SlabBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE));

    public static final Block POLISHED_CALCITE_WALL = registerBlock("polished_calcite_wall",
            WallBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE));

    public static final Block CALCITE_BRICKS = registerBlock("calcite_bricks",
            Block::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE));

    public static final Block CALCITE_BRICK_STAIRS = registerBlock("calcite_brick_stairs",
            p -> new StairBlock(CALCITE_BRICKS.defaultBlockState(), p),
            BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE));

    public static final Block CALCITE_BRICK_SLAB = registerBlock("calcite_brick_slab",
            SlabBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE));

    public static final Block CALCITE_BRICK_WALL = registerBlock("calcite_brick_wall",
            WallBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE));

    public static final Block POLISHED_TUFF = registerBlock("polished_tuff",
            Block::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF));

    public static final Block POLISHED_TUFF_STAIRS = registerBlock("polished_tuff_stairs",
            p -> new StairBlock(POLISHED_TUFF.defaultBlockState(), p),
            BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF));

    public static final Block POLISHED_TUFF_SLAB = registerBlock("polished_tuff_slab",
            SlabBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF));

    public static final Block POLISHED_TUFF_WALL = registerBlock("polished_tuff_wall",
            WallBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF));

    public static final Block TUFF_BRICKS = registerBlock("tuff_bricks",
            Block::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF));

    public static final Block TUFF_BRICK_STAIRS = registerBlock("tuff_brick_stairs",
            p -> new StairBlock(TUFF_BRICKS.defaultBlockState(), p),
            BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF));

    public static final Block TUFF_BRICK_SLAB = registerBlock("tuff_brick_slab",
            SlabBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF));

    public static final Block TUFF_BRICK_WALL = registerBlock("tuff_brick_wall",
            WallBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF));

    public static final Block SMOOTH_BASALT_STAIRS = registerBlock("smooth_basalt_stairs",
            p -> new StairBlock(Blocks.SMOOTH_BASALT.defaultBlockState(), p),
            BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_BASALT));

    public static final Block SMOOTH_BASALT_SLAB = registerBlock("smooth_basalt_slab",
            SlabBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_BASALT));

    public static final Block SMOOTH_BASALT_WALL = registerBlock("smooth_basalt_wall",
            WallBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_BASALT));

    public static final Block SMOOTH_SANDSTONE_WALL = registerBlock("smooth_sandstone_wall",
            WallBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_SANDSTONE));

    public static final Block SMOOTH_RED_SANDSTONE_WALL = registerBlock("smooth_red_sandstone_wall",
            WallBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_RED_SANDSTONE));

    public static final Block POLISHED_ANDESITE_WALL = registerBlock("polished_andesite_wall",
            WallBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_ANDESITE));

    public static final Block POLISHED_DIORITE_WALL = registerBlock("polished_diorite_wall",
            WallBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_DIORITE));

    public static final Block POLISHED_GRANITE_WALL = registerBlock("polished_granite_wall",
            WallBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_GRANITE));

    public static final Block QUARTZ_WALL = registerBlock("quartz_wall",
            WallBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK));

    public static final Block SMOOTH_QUARTZ_WALL = registerBlock("smooth_quartz_wall",
            WallBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ));

    public static final Block MUG = registerBlock("mug",
            MugBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).noOcclusion().sound(SoundType.WOOD).strength(0.5F));

    public static final Block LAVA_MUG = registerBlock("lava_mug",
            LavaMugBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).noOcclusion().sound(SoundType.WOOD).strength(0.5F).lightLevel(LavaMugBlock::getLight));

    public static final Block MILK_MUG = registerBlock("milk_mug",
            MilkMugBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).noOcclusion().sound(SoundType.WOOD).strength(0.5F));

    public static final Block POTION_MUG = registerBlock("potion_mug",
            p -> new PotionMugBlock(p, false),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).noOcclusion().sound(SoundType.WOOD).strength(0.5F));

    public static final Block PILK_MUG = registerBlock("pilk_mug",
            p -> new PotionMugBlock(p, true),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).noOcclusion().sound(SoundType.WOOD).strength(0.5F));

    public static final Block POWDER_SNOW_MUG = registerBlock("powder_snow_mug",
            PowderSnowMugBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).noOcclusion().sound(SoundType.WOOD).strength(0.5F));

    public static final Block HONEY_MUG = registerBlock("honey_mug",
            HoneyMugBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).noOcclusion().sound(SoundType.WOOD).strength(0.5F));

    public static final Block STRIPPED_OAK_BRANCH = registerBlock("stripped_oak_branch",
            p -> new BranchBlock(p),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)
                    .mapColor(MapColor.WOOD)
                    .strength(1.5F));

    public static final Block STRIPPED_SPRUCE_BRANCH = registerBlock("stripped_spruce_branch",
            p -> new BranchBlock(p),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_SPRUCE_LOG)
                    .mapColor(MapColor.PODZOL)
                    .strength(1.5F));

    public static final Block STRIPPED_BIRCH_BRANCH = registerBlock("stripped_birch_branch",
            p -> new BranchBlock(p),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_BIRCH_LOG)
                    .mapColor(MapColor.SAND)
                    .strength(1.5F));

    public static final Block STRIPPED_JUNGLE_BRANCH = registerBlock("stripped_jungle_branch",
            p -> new BranchBlock(p),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_JUNGLE_LOG)
                    .mapColor(MapColor.DIRT)
                    .strength(1.5F));

    public static final Block STRIPPED_ACACIA_BRANCH = registerBlock("stripped_acacia_branch",
            p -> new BranchBlock(p),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_ACACIA_LOG)
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(1.5F));

    public static final Block STRIPPED_DARK_OAK_BRANCH = registerBlock("stripped_dark_oak_branch",
            p -> new BranchBlock(p),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_DARK_OAK_LOG)
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(1.5F));

    public static final Block STRIPPED_CRIMSON_BRANCH = registerBlock("stripped_crimson_branch",
            p -> new BranchBlock(p),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CRIMSON_STEM)
                    .mapColor(MapColor.CRIMSON_STEM)
                    .strength(1.5F));

    public static final Block STRIPPED_WARPED_BRANCH = registerBlock("stripped_warped_branch",
            p -> new BranchBlock(p),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_WARPED_STEM)
                    .mapColor(MapColor.WARPED_STEM)
                    .strength(1.5F));

    public static final Block STRIPPED_MANGROVE_BRANCH = registerBlock("stripped_mangrove_branch",
            p -> new BranchBlock(p),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_MANGROVE_LOG)
                    .mapColor(MapColor.COLOR_RED)
                    .strength(1.5F));

    public static final Block OAK_BRANCH = registerBlock("oak_branch",
            p -> new BranchBlock(p, () -> STRIPPED_OAK_BRANCH),
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)
                    .mapColor(MapColor.PODZOL)
                    .strength(1.5F));

    public static final Block SPRUCE_BRANCH = registerBlock("spruce_branch",
            p -> new BranchBlock(p, () -> STRIPPED_SPRUCE_BRANCH),
            BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_LOG)
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(1.5F));

    public static final Block BIRCH_BRANCH = registerBlock("birch_branch",
            p -> new BranchBlock(p, () -> STRIPPED_BIRCH_BRANCH),
            BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_LOG)
                    .mapColor(MapColor.SAND)
                    .strength(1.5F));

    public static final Block JUNGLE_BRANCH = registerBlock("jungle_branch",
            p -> new BranchBlock(p, () -> STRIPPED_JUNGLE_BRANCH),
            BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_LOG)
                    .mapColor(MapColor.PODZOL)
                    .strength(1.5F));

    public static final Block ACACIA_BRANCH = registerBlock("acacia_branch",
            p -> new BranchBlock(p, () -> STRIPPED_ACACIA_BRANCH),
            BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_LOG)
                    .mapColor(MapColor.STONE)
                    .strength(1.5F));

    public static final Block DARK_OAK_BRANCH = registerBlock("dark_oak_branch",
            p -> new BranchBlock(p, () -> STRIPPED_DARK_OAK_BRANCH),
            BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_LOG)
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(1.5F));

    public static final Block CRIMSON_BRANCH = registerBlock("crimson_branch",
            p -> new BranchBlock(p, () -> STRIPPED_CRIMSON_BRANCH),
            BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_STEM)
                    .mapColor(MapColor.CRIMSON_STEM)
                    .strength(1.5F));

    public static final Block WARPED_BRANCH = registerBlock("warped_branch",
            p -> new BranchBlock(p, () -> STRIPPED_WARPED_BRANCH),
            BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_STEM)
                    .mapColor(MapColor.WARPED_STEM)
                    .strength(1.5F));

    public static final Block MANGROVE_BRANCH = registerBlock("mangrove_branch",
            p -> new BranchBlock(p, () -> STRIPPED_MANGROVE_BRANCH),
            BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_LOG)
                    .mapColor(MapColor.PODZOL)
                    .strength(1.5F));

    public static final Block AQUARIUM_GLASS = registerBlock("aquarium_glass",
            AquariumGlassBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS));

    public static final Block EQUIVALENCE_DETECTOR = registerBlock("equivalence_detector",
            EquivalenceDetectorBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));

    private static BlockBehaviour.Properties castIron() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_GRAY)
                .requiresCorrectToolForDrops()
                .strength(4.0F, 6.0F)
                .sound(SoundType.COPPER);
    }

    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory,
                                       BlockBehaviour.Properties properties) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK,
                Identifier.fromNamespaceAndPath(Entropy.MOD_ID, name));
        return Registry.register(BuiltInRegistries.BLOCK, key, factory.apply(properties.setId(key)));
    }

    public static void initialize() {

    }
}