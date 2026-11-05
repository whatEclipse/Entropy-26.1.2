package net.entropy.blockentities;

import net.entropy.registry.EntropyBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.ArrayList;
import java.util.List;

public class PotionMugBlockEntity extends BlockEntity {
    private List<MobEffectInstance> effects = new ArrayList<>();

    public PotionMugBlockEntity(BlockPos pos, BlockState state) {
        super(EntropyBlockEntities.POTION_MUG, pos, state);
    }

    public List<MobEffectInstance> getEffects() {
        return effects;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("Effects", MobEffectInstance.CODEC.listOf(), effects);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        effects = new ArrayList<>(input.read("Effects", MobEffectInstance.CODEC.listOf()).orElse(List.of()));
    }

    private void sync() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public void addEffects(Iterable<MobEffectInstance> added) {
        for (MobEffectInstance n : added) {
            effects.removeIf(e -> e.getEffect().equals(n.getEffect())
                    && n.getAmplifier() >= e.getAmplifier() && n.getDuration() >= e.getDuration());
            if (effects.stream().noneMatch(e -> e.getEffect().equals(n.getEffect()))) {
                effects.add(new MobEffectInstance(n));
            }
        }
        sync();
    }

    /** One third of the mug: applies a third of each effect duration and weakens what remains. */
    public void drink(Player player) {
        List<MobEffectInstance> remaining = new ArrayList<>();
        for (MobEffectInstance e : effects) {
            player.addEffect(new MobEffectInstance(e.getEffect(), e.getDuration() / 3, e.getAmplifier(),
                    e.isAmbient(), e.isVisible(), e.showIcon()));
            remaining.add(new MobEffectInstance(e.getEffect(), e.getDuration() * 2 / 3, Math.max(0, e.getAmplifier() - 1),
                    e.isAmbient(), e.isVisible(), e.showIcon()));
        }
        effects = remaining;
        sync();
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }
}
