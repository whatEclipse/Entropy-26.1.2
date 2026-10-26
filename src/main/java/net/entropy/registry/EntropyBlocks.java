package net.entropy.registry;

import java.util.function.Function;
import net.entropy.Entropy;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
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
            properties -> new StairBlock(AMETHYST_BRICKS.defaultBlockState(), properties),
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

    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory,
                                       BlockBehaviour.Properties properties) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK,
                Identifier.fromNamespaceAndPath(Entropy.MOD_ID, name));
        return Registry.register(BuiltInRegistries.BLOCK, key, factory.apply(properties.setId(key)));
    }

    public static void initialize() {

    }
}