package net.entropy.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.Optional;
import java.util.function.Consumer;

public record ContractSigners(Optional<String> firstSigner, Optional<String> secondSigner) implements TooltipProvider {
    public static final ContractSigners EMPTY = new ContractSigners(Optional.empty(), Optional.empty());

    public static final Codec<ContractSigners> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.optionalFieldOf("first_signer").forGetter(ContractSigners::firstSigner),
            Codec.STRING.optionalFieldOf("second_signer").forGetter(ContractSigners::secondSigner)
    ).apply(i, ContractSigners::new));

    public static final StreamCodec<io.netty.buffer.ByteBuf, ContractSigners> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs::optional), ContractSigners::firstSigner,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs::optional), ContractSigners::secondSigner,
            ContractSigners::new
    );

    public boolean isFullySigned() {
        return firstSigner.isPresent() && secondSigner.isPresent();
    }

    public ContractSigners withFirstSigner(String name) {
        return new ContractSigners(Optional.of(name), this.secondSigner);
    }

    public ContractSigners withSecondSigner(String name) {
        return new ContractSigners(this.firstSigner, Optional.of(name));
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        firstSigner.ifPresent(name -> consumer.accept(Component.translatable("entropy.contract.signed_by", name)));
        secondSigner.ifPresent(name -> consumer.accept(Component.translatable("entropy.contract.witnessed_by", name)));
    }
}