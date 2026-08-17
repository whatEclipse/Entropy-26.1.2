package net.entropy.datagen;

import net.entropy.Entropy;
import net.entropy.sounds.EntropySounds;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.world.item.JukeboxSong;

public class EntropyJukeboxSongs {
    public static final ResourceKey<JukeboxSong> DELIRIO_KEY = ResourceKey.create(Registries.JUKEBOX_SONG,
            Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "delirio"));

    public static final ResourceKey<JukeboxSong> PERFECT_FUNERAL_KEY = ResourceKey.create(Registries.JUKEBOX_SONG,
            Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "perfect_funeral"));

    public static final ResourceKey<JukeboxSong> I_WILL_NEVER_BE_ON_YOUR_SIDE_AGAIN_KEY = ResourceKey.create(Registries.JUKEBOX_SONG,
            Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "i_will_never_be_on_your_side_again"));

    public static void bootstrap(BootstrapContext<JukeboxSong> bootstrapContext) {
        register(bootstrapContext, DELIRIO_KEY, EntropySounds.DELIRIO, 227, 15);
        register(bootstrapContext, I_WILL_NEVER_BE_ON_YOUR_SIDE_AGAIN_KEY, EntropySounds.I_WILL_NEVER_BE_ON_YOUR_SIDE_AGAIN, 327, 0);
        register(bootstrapContext, PERFECT_FUNERAL_KEY, EntropySounds.PERFECT_FUNERAL, 374, 0);
    }

    private static void register(BootstrapContext<JukeboxSong> bootstrapContext, ResourceKey<JukeboxSong> key,
                                 Holder.Reference<SoundEvent> soundEventHolder, int lenghtInSeconds, int comparatorOutput) {
        bootstrapContext.register(key, new JukeboxSong(soundEventHolder,
                Component.translatable(Util.makeDescriptionId("jukebox_song", key.identifier())), lenghtInSeconds, comparatorOutput));
    }
}
