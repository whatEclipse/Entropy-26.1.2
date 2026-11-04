package net.entropy.registry;

import java.util.function.Function;
import net.entropy.Entropy;
import net.entropy.blocks.CastIronCrateBlock;
import net.entropy.blocks.RatMaidPlushieBlock;
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
