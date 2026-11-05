package net.entropy.registry;

import net.entropy.Entropy;
import net.entropy.blockentities.PotionMugBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class EntropyBlockEntities {
    public static final BlockEntityType<PotionMugBlockEntity> POTION_MUG = register("potion_mug",
            FabricBlockEntityTypeBuilder.create(PotionMugBlockEntity::new, EntropyBlocks.POTION_MUG, EntropyBlocks.PILK_MUG).build());

    private static <T extends BlockEntityType<?>> T register(String name, T type) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(Entropy.MOD_ID, name), type);
    }

    public static void initialize() {
    }
}
