package net.entropy;

import net.entropy.creativetab.EntropyCreativeTab;
import net.entropy.datacomponents.ContractSigners;
import net.entropy.particles.EntropyParticles;
import net.entropy.registry.*;
import net.entropy.loottable.EntropyLootTableModifier;
import net.entropy.network.WitnessSignPayload;
import net.entropy.sounds.EntropySounds;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Entropy implements ModInitializer {
	public static final String MOD_ID = "entropy";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		EntropyItems.initialize();
		EntropyEntities.initialize();
		EntropyParticles.initialize();
		EntropySounds.initialize();
		EntropyCreativeTab.initialize();
		EntropyDataComponents.initialize();
		EntropyEffects.initialize();

		LootTableEvents.MODIFY.register(EntropyLootTableModifier::modifyLootTables);

		PayloadTypeRegistry.serverboundPlay().register(WitnessSignPayload.TYPE, WitnessSignPayload.STREAM_CODEC);

		ServerPlayNetworking.registerGlobalReceiver(WitnessSignPayload.TYPE, (payload, context) -> {
			context.server().execute(() -> {
				var player = context.player();
				ItemStack stack = player.getItemInHand(payload.hand());

				if (stack.getItem() != EntropyItems.OPEN_CONTRACT) return;

				ContractSigners signers = stack.getOrDefault(EntropyDataComponents.CONTRACT_SIGNERS, ContractSigners.EMPTY);

				if (signers.firstSigner().isEmpty()) return;
				if (signers.firstSigner().get().equals(player.getGameProfile().name())) return;
				if (signers.secondSigner().isPresent()) return;

				ContractSigners updated = signers.withSecondSigner(player.getGameProfile().name());

				ItemStack closed = stack.transmuteCopy(EntropyItems.CLOSED_CONTRACT);
				closed.set(EntropyDataComponents.CONTRACT_SIGNERS, updated);

				List<Component> loreLines = List.of(
						Component.translatable("entropy.contract.signed_by", updated.firstSigner().orElse("")),
						Component.translatable("entropy.contract.witnessed_by", updated.secondSigner().orElse(""))
				);
				closed.set(DataComponents.LORE, new ItemLore(loreLines));

				player.setItemInHand(payload.hand(), closed);
			});
		});


		LOGGER.info("Fucking hell who caused all this *CHAOS*?! Haha, get the joke? No?... :( ");
	}
}