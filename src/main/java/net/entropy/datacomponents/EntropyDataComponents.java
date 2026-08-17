package net.entropy.datacomponents;

import net.entropy.Entropy;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class EntropyDataComponents {
    public static final DataComponentType<ContractSigners> CONTRACT_SIGNERS = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "contract_signers"),
            DataComponentType.<ContractSigners>builder()
                    .persistent(ContractSigners.CODEC)
                    .networkSynchronized(ContractSigners.STREAM_CODEC)
                    .build()
    );

    public static void initialize() {
    }
}