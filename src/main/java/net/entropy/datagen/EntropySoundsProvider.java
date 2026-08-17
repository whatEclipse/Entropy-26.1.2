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

    @Override
    protected void configure(HolderLookup.Provider registryLookup, SoundExporter exporter) {
        exporter.add(EntropySounds.DELIRIO, SoundTypeBuilder.of(EntropySounds.DELIRIO.value()).subtitle("entropy.subtitle.delirio_playing")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "delirio")).stream(true)));

        exporter.add(EntropySounds.PERFECT_FUNERAL, SoundTypeBuilder.of(EntropySounds.PERFECT_FUNERAL.value()).subtitle("entropy.subtitle.perfect_funeral_playing")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "perfect_funeral")).stream(true)));

        exporter.add(EntropySounds.I_WILL_NEVER_BE_ON_YOUR_SIDE_AGAIN, SoundTypeBuilder.of(EntropySounds.I_WILL_NEVER_BE_ON_YOUR_SIDE_AGAIN.value()).subtitle("entropy.subtitle.i_will_never_be_on_your_side_again_playing")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "i_will_never_be_on_your_side_again.")).stream(true)));

    }

    @Override
    public String getName() {
        return "";
    }
}
