package net.entropy.registry;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.world.level.block.Block;

/** Fabric registry hooks for the ported blocks. Call {@link #initialize()} after EntropyBlocks. */
public class EntropyBlockExtras {
    public static void initialize() {
        Block[] branches = {
                EntropyBlocks.OAK_BRANCH, EntropyBlocks.SPRUCE_BRANCH, EntropyBlocks.BIRCH_BRANCH, EntropyBlocks.JUNGLE_BRANCH,
                EntropyBlocks.ACACIA_BRANCH, EntropyBlocks.DARK_OAK_BRANCH, EntropyBlocks.MANGROVE_BRANCH,
                EntropyBlocks.STRIPPED_OAK_BRANCH, EntropyBlocks.STRIPPED_SPRUCE_BRANCH, EntropyBlocks.STRIPPED_BIRCH_BRANCH,
                EntropyBlocks.STRIPPED_JUNGLE_BRANCH, EntropyBlocks.STRIPPED_ACACIA_BRANCH,
                EntropyBlocks.STRIPPED_DARK_OAK_BRANCH, EntropyBlocks.STRIPPED_MANGROVE_BRANCH};
        for (Block b : branches) {
            FlammableBlockRegistry.getDefaultInstance().add(b, 10, 5);
        }
        FlammableBlockRegistry.getDefaultInstance().add(EntropyBlocks.GIANT_ACORN_BLOCK, 5, 20);
    }
}
