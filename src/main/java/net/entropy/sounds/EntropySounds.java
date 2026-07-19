package net.entropy.sounds;

import net.entropy.Entropy;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class EntropySounds {

    public static final Map<SoundEvent, Identifier> SOUND_EVENTS = new LinkedHashMap<>();

    public static final SoundEvent ITEM_SCYTHE_HIT = create("item.scythe.hit");
    public static final SoundEvent ITEM_SCYTHE_SPEWING = create("item.scythe.spewing");
    public static final SoundEvent ENTITY_BLOOD_SCYTHE_HIT = create("entity.blood_scythe.hit");

    private static SoundEvent create(String path) {
        Identifier id = Identifier.fromNamespaceAndPath(Entropy.MOD_ID, path);

        SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(id);
        SOUND_EVENTS.put(soundEvent, id);
        return soundEvent;
    }

    public static void initialize() {
        SOUND_EVENTS.forEach((soundEvent, id) -> Registry.register(BuiltInRegistries.SOUND_EVENT, id, soundEvent));
    }
}
