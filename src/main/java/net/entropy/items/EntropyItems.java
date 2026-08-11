package net.entropy.items;

import java.util.function.Function;
import net.entropy.Entropy;
import net.entropy.blocks.EntropyBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class EntropyItems {

    public static final Item SCYTHE = registerItem("scythe", properties -> new ScytheItem(properties));
    public static final Item SHARK_TOOTH_CLEAVER = registerItem("shark_tooth_cleaver", properties -> new SharkToothCleaverItem(properties));
    public static final Item OPEN_CONTRACT = registerItem("open_contract", properties -> new OpenContractItem(properties));
    public static final Item CLOSED_CONTRACT = registerItem("closed_contract", properties -> new Item(properties));
    public static final Item BUTTER = registerItem("butter", properties -> new Item(properties));

    public static final Item AMETHYST_BRICKS = registerBlockItem("amethyst_bricks", EntropyBlocks.AMETHYST_BRICKS);
    public static final Item AMETHYST_BRICK_STAIRS = registerBlockItem("amethyst_brick_stairs", EntropyBlocks.AMETHYST_BRICK_STAIRS);
    public static final Item AMETHYST_BRICK_SLAB = registerBlockItem("amethyst_brick_slab", EntropyBlocks.AMETHYST_BRICK_SLAB);
    public static final Item AMETHYST_BRICK_WALL = registerBlockItem("amethyst_brick_wall", EntropyBlocks.AMETHYST_BRICK_WALL);
    public static final Item AMETHYST_PILLAR = registerBlockItem("amethyst_pillar", EntropyBlocks.AMETHYST_PILLAR);
    public static final Item CHISELED_AMETHYST = registerBlockItem("chiseled_amethyst", EntropyBlocks.CHISELED_AMETHYST);

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


    public static void initialize() {
    }
}
