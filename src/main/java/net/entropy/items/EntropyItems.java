package net.entropy.items;

import java.util.function.Function;
import net.entropy.Entropy;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

public class EntropyItems {

    public static final Item SCYTHE = registerItem("scythe", properties -> new ScytheItem(properties));

    //Register method for items
    private static Item registerItem(String name, Function<Item.Properties, Item> properties) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(Entropy.MOD_ID, name),
                properties.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM,
                        Identifier.fromNamespaceAndPath(Entropy.MOD_ID, name)))));

    }


    public static void initialize() {

    }
}
