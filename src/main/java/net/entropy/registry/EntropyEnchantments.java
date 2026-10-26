package net.entropy.registry;

import net.entropy.Entropy;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class EntropyEnchantments {
    public static final ResourceKey<Enchantment> SPEWING = ResourceKey.create(
            Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "spewing"));
    public static final ResourceKey<Enchantment> REELING = ResourceKey.create(
            Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "reeling"));

}
