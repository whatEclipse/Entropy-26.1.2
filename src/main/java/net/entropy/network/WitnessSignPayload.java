package net.entropy.network;

import net.entropy.Entropy;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;

public record WitnessSignPayload(InteractionHand hand) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<WitnessSignPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Entropy.MOD_ID, "witness_sign"));

    public static final StreamCodec<RegistryFriendlyByteBuf, WitnessSignPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.idMapper(i -> InteractionHand.values()[i], InteractionHand::ordinal), WitnessSignPayload::hand,
            WitnessSignPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}