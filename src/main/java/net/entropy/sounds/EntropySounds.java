package net.entropy.sounds;

import net.entropy.Entropy;
import net.minecraft.core.Holder;
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
    public static final SoundEvent RAT_MAID_PLUSHIE_HONK = create("block.rat_maid_plushie.honk");

    public static final Holder.Reference<SoundEvent> DELIRIO = registerJukeBoxSong("delirio");
    public static final Holder.Reference<SoundEvent> I_WILL_NEVER_BE_ON_YOUR_SIDE_AGAIN = registerJukeBoxSong("i_will_never_be_on_your_side_again");
    public static final Holder.Reference<SoundEvent> PERFECT_FUNERAL = registerJukeBoxSong("perfect_funeral");

    public static Holder.Reference<SoundEvent> registerJukeBoxSong(String name){
        Identifier identifier = Identifier.fromNamespaceAndPath(Entropy.MOD_ID, name);
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

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
