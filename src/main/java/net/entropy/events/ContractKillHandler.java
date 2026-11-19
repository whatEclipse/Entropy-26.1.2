package net.entropy.events;

import net.entropy.datacomponents.ContractSigners;
import net.entropy.registry.EntropyCriteria;
import net.entropy.registry.EntropyDataComponents;
import net.entropy.registry.EntropyItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ContractKillHandler {

    public static void register() {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (!(entity instanceof ServerPlayer victim)) return;
            if (!(source.getEntity() instanceof ServerPlayer killer)) return;
            if (killer == victim) return;

            String killerName = killer.getGameProfile().name();
            String victimName = victim.getGameProfile().name();

            for (int i = 0; i < killer.getInventory().getContainerSize(); i++) {
                ItemStack stack = killer.getInventory().getItem(i);
                if (stack.getItem() != EntropyItems.CLOSED_CONTRACT) continue;

                ContractSigners signers = stack.getOrDefault(EntropyDataComponents.CONTRACT_SIGNERS, ContractSigners.EMPTY);
                if (signers.isBetween(killerName, victimName)) {
                    EntropyCriteria.CONTRACT_SIGNER_KILL.trigger(killer);
                    return;
                }
            }
        });
    }
}