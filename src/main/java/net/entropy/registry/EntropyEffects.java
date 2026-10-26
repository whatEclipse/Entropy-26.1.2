package net.entropy.registry;

import net.entropy.Entropy;
import net.entropy.effects.StunnedEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;

public class EntropyEffects {

    public static final Holder<MobEffect> STUNNED = register("stunned", new StunnedEffect());

    private static Holder<MobEffect> register(String name, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT,
                Identifier.fromNamespaceAndPath(Entropy.MOD_ID, name), effect);
    }

    public static void initialize() {
        StunnedEffect.registerEvents();
    }
}