package net.entropy.datagen;

import net.entropy.Entropy;
import net.entropy.sounds.EntropySounds;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.concurrent.CompletableFuture;

public class EntropySoundsProvider extends FabricSoundsProvider {
    public EntropySoundsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(Entropy.MOD_ID, path);
    }

    @Override
    protected void configure(HolderLookup.Provider registryLookup, SoundExporter exporter) {
        // Music discs (streamed)
        exporter.add(EntropySounds.DELIRIO, SoundTypeBuilder.of(EntropySounds.DELIRIO.value())
                .subtitle("entropy.subtitle.delirio_playing")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(id("delirio")).stream(true)));

        exporter.add(EntropySounds.PERFECT_FUNERAL, SoundTypeBuilder.of(EntropySounds.PERFECT_FUNERAL.value())
                .subtitle("entropy.subtitle.perfect_funeral_playing")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(id("perfect_funeral")).stream(true)));

        exporter.add(EntropySounds.I_WILL_NEVER_BE_ON_YOUR_SIDE_AGAIN, SoundTypeBuilder.of(EntropySounds.I_WILL_NEVER_BE_ON_YOUR_SIDE_AGAIN.value())
                .subtitle("entropy.subtitle.i_will_never_be_on_your_side_again_playing")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(id("i_will_never_be_on_your_side_again")).stream(true)));

        // Scythe
        exporter.add(EntropySounds.ITEM_SCYTHE_HIT, SoundTypeBuilder.of(EntropySounds.ITEM_SCYTHE_HIT)
                .subtitle("entropy.subtitles.item.scythe.hit")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(id("item/scythe_hit"))));

        exporter.add(EntropySounds.ITEM_SCYTHE_SPEWING, SoundTypeBuilder.of(EntropySounds.ITEM_SCYTHE_SPEWING)
                .subtitle("entropy.subtitles.item.scythe.spewing")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(id("item/scythe_spewing_1")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(id("item/scythe_spewing_2"))));

        exporter.add(EntropySounds.ENTITY_BLOOD_SCYTHE_HIT, SoundTypeBuilder.of(EntropySounds.ENTITY_BLOOD_SCYTHE_HIT)
                .subtitle("entropy.subtitles.entity.blood_scythe.hit")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(id("entity/blood_scythe_hit"))));

        // Blocks
        exporter.add(EntropySounds.RAT_MAID_PLUSHIE_HONK, SoundTypeBuilder.of(EntropySounds.RAT_MAID_PLUSHIE_HONK)
                .subtitle("subtitles.entropy.block.rat_maid_plushie.honk")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(id("rat_maid_plushie_honk"))));
    }

    @Override
    public String getName() {
        return "Entropy Sounds";
    }
}