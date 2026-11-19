package net.entropy.registry;

import net.entropy.Entropy;
import net.entropy.advancement.ContractSignerKillTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class EntropyCriteria {
    public static final ContractSignerKillTrigger CONTRACT_SIGNER_KILL = Registry.register(
            BuiltInRegistries.TRIGGER_TYPES,
            Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "contract_signer_kill"),
            new ContractSignerKillTrigger()
    );

    public static void initialize() {
    }
}