package net.entropy;

import net.entropy.client.gui.ClosedContractScreen;
import net.entropy.client.gui.ContractReadOnlyScreen;
import net.entropy.client.gui.ContractWitnessScreen;
import net.entropy.client.gui.OpenContractScreen;
import net.entropy.datacomponents.ContractSigners;
import net.entropy.datacomponents.EntropyDataComponents;
import net.entropy.entity.BloodScytheRenderer;
import net.entropy.entity.EntropyEntities;
import net.entropy.entity.ThrownAtlanTridentRenderer;
import net.entropy.items.EntropyItems;
import net.entropy.particles.EntropyParticlesClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WritableBookContent;

public class EntropyClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntropyParticlesClient.registerFactories();
		UseItemCallback.EVENT.register((player, world, hand) -> {
			ItemStack stack = player.getItemInHand(hand);

			if (world.isClientSide() && stack.getItem() == EntropyItems.CLOSED_CONTRACT) {
				Minecraft.getInstance().setScreen(new ClosedContractScreen(stack));
				return InteractionResult.SUCCESS;
			}

			if (world.isClientSide() && stack.getItem() == EntropyItems.OPEN_CONTRACT) {
				ContractSigners signers = stack.getOrDefault(EntropyDataComponents.CONTRACT_SIGNERS, ContractSigners.EMPTY);
				String myName = player.getGameProfile().name();

				if (signers.firstSigner().isEmpty()) {
					WritableBookContent content = stack.getOrDefault(DataComponents.WRITABLE_BOOK_CONTENT, WritableBookContent.EMPTY);
					Minecraft.getInstance().setScreen(new OpenContractScreen(player, stack, hand, content));
				} else if (signers.firstSigner().get().equals(myName)) {
					WritableBookContent content = stack.getOrDefault(DataComponents.WRITABLE_BOOK_CONTENT, WritableBookContent.EMPTY);
					Minecraft.getInstance().setScreen(new OpenContractScreen(player, stack, hand, content));
				} else if (signers.secondSigner().isEmpty()) {
					Minecraft.getInstance().setScreen(new ContractWitnessScreen(stack, hand));
				}
				return InteractionResult.SUCCESS;
			}

			return InteractionResult.PASS;
		});
		EntityRendererRegistry.register(EntropyEntities.BLOOD_SCYTHE, BloodScytheRenderer::new);
		EntityRendererRegistry.register(EntropyEntities.THROWN_ATLAN_TRIDENT, ThrownAtlanTridentRenderer::new);
	}
}