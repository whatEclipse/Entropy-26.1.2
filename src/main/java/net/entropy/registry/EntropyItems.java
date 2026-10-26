package net.entropy.registry;

import java.util.function.Function;
import net.entropy.Entropy;
import net.entropy.datagen.EntropyJukeboxSongs;
import net.entropy.items.contracts.ClosedContractItem;
import net.entropy.items.contracts.OpenContractItem;
import net.entropy.items.weapons.AtlanTridentItem;
import net.entropy.items.weapons.ScytheItem;
import net.entropy.items.weapons.SharkToothCleaverItem;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.level.block.Block;

public class EntropyItems {

    public static final Item SCYTHE = registerItem("scythe", properties -> new ScytheItem(properties));
    public static final Item ATLAN_TRIDENT = registerItem("atlan_trident", properties -> new AtlanTridentItem(properties));
    public static final Item SHARK_TOOTH_CLEAVER = registerItem("shark_tooth_cleaver", properties -> new SharkToothCleaverItem(properties));

    public static final Item OPEN_CONTRACT = Registry.register(
            BuiltInRegistries.ITEM,
            Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "open_contract"),
            new OpenContractItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "open_contract")))
                    .component(DataComponents.WRITABLE_BOOK_CONTENT, WritableBookContent.EMPTY))
    );
    public static final Item CLOSED_CONTRACT = registerItem("closed_contract", ClosedContractItem::new);

    public static final Item BUTTER = registerItem("butter", properties -> new Item(properties));
    public static final Item MUSIC_DISC_DELIRIO = registerItem("music_disc_delirio", properties
            -> new Item(properties.jukeboxPlayable(EntropyJukeboxSongs.DELIRIO_KEY).stacksTo(1).rarity(Rarity.RARE)));
    //public static final Item MUSIC_DISC_I_WILL_NEVER_BE_ON_YOUR_SIDE_AGAIN = registerItem("music_disc_i_will_never_be_on_your_side_again", properties
    //        -> new Item(properties.jukeboxPlayable(EntropyJukeboxSongs.I_WILL_NEVER_BE_ON_YOUR_SIDE_AGAIN_KEY).stacksTo(1).rarity(Rarity.EPIC)));
    //public static final Item MUSIC_DISC_PERFECT_FUNERAL = registerItem("music_disc_perfect_funeral", properties
    //      -> new Item(properties.jukeboxPlayable(EntropyJukeboxSongs.PERFECT_FUNERAL_KEY).stacksTo(1).rarity(Rarity.EPIC)));

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
