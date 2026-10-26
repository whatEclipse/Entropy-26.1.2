package net.entropy.registry;

import net.entropy.Entropy;
import net.entropy.entity.projectile.ThrownAtlanTridentEntity;
import net.entropy.entity.projectile.BloodScytheEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.LinkedHashMap;
import java.util.Map;

public class EntropyEntities {

    public static final Map<EntityType<? extends Entity>, Identifier> ENTITIES = new LinkedHashMap<>();

    public static final EntityType<BloodScytheEntity> BLOOD_SCYTHE = create("blood_scythe",
            EntityType.Builder.<BloodScytheEntity>of(BloodScytheEntity::new, MobCategory.MISC)
                    .noSave()
                    .sized(5.0F, 0.2F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "blood_scythe")))
    );

    public static final EntityType<ThrownAtlanTridentEntity> THROWN_ATLAN_TRIDENT = create("thrown_atlan_trident",
            EntityType.Builder.<ThrownAtlanTridentEntity>of(ThrownAtlanTridentEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "thrown_atlan_trident"))
    ));

    private static <T extends EntityType<? extends Entity>> T create(String name, T entity) {
        ENTITIES.put(entity, Identifier.fromNamespaceAndPath(Entropy.MOD_ID, name));
        return entity;
    }

    public static void initialize() {
        ENTITIES.forEach((entityType, id) -> Registry.register(BuiltInRegistries.ENTITY_TYPE, id, entityType));

    }
}