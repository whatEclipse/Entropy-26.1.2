package net.entropy.particles;

import net.entropy.Entropy;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class EntropyParticles {

    public static final Map<SimpleParticleType, Identifier> PARTICLES = new LinkedHashMap<>();

    public static final SimpleParticleType BLOOD_BUBBLE = create("blood_bubble", FabricParticleTypes.simple(true));

    public static final SimpleParticleType BLOOD_BUBBLE_SPLATTER = create("blood_bubble_splatter", FabricParticleTypes.simple(true));

    private static SimpleParticleType create(String name, SimpleParticleType particle) {
        PARTICLES.put(particle, Identifier.fromNamespaceAndPath(Entropy.MOD_ID, name));
        return particle;
    }

    public static void initialize() {
        PARTICLES.forEach((particle, id) -> Registry.register(BuiltInRegistries.PARTICLE_TYPE, id, particle));
    }
}