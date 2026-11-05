package net.entropy.blocks.mug;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.entropy.blockentities.PotionMugBlockEntity;
import net.entropy.registry.EntropyBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

/** Water/potion mug (isPilk = false) or milk-and-potion "pilk" mug (isPilk = true). */
public class PotionMugBlock extends FilledMugBlock implements EntityBlock {
    public static final MapCodec<PotionMugBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            propertiesCodec(), Codec.BOOL.fieldOf("is_pilk").forGetter(b -> b.isPilk)
    ).apply(i, PotionMugBlock::new));
    private final boolean isPilk;

    public PotionMugBlock(BlockBehaviour.Properties properties, boolean isPilk) {
        super(properties);
        this.isPilk = isPilk;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PotionMugBlockEntity(pos, state);
    }

    @Override
    protected boolean canFill(BlockState state, ItemStack stack) {
        return (stack.is(Items.WATER_BUCKET) || stack.is(Items.MILK_BUCKET) || stack.is(Items.POTION))
                && super.canFill(state, stack);
    }

    @Override
    protected void fill(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        if (stack.is(Items.POTION)) {
            PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
            setFilled(this, state.getValue(LEVEL) + 1, state, level, pos);
            if (contents != null && level.getBlockEntity(pos) instanceof PotionMugBlockEntity be) {
                be.addEffects(contents.getAllEffects());
            }
            give(player, hand, stack, new ItemStack(Items.GLASS_BOTTLE));
        } else if (stack.is(Items.MILK_BUCKET)) {
            if (level.getBlockEntity(pos) instanceof PotionMugBlockEntity be) {
                var effects = java.util.List.copyOf(be.getEffects());
                setFilled(EntropyBlocks.PILK_MUG, 3, state, level, pos);
                if (level.getBlockEntity(pos) instanceof PotionMugBlockEntity pilk) {
                    pilk.addEffects(effects);
                }
                give(player, hand, stack, new ItemStack(Items.BUCKET));
            }
        } else {
            setFilled(this, 3, state, level, pos);
            give(player, hand, stack, new ItemStack(Items.BUCKET));
        }
    }

    @Override
    protected SoundEvent getDrinkSound() {
        return isPilk ? SoundEvents.WANDERING_TRADER_DRINK_MILK : super.getDrinkSound();
    }

    @Override
    protected void drink(Player player, BlockState state, Level level, BlockPos pos) {
        if (isPilk) {
            player.removeAllEffects();
        }
        if (level.getBlockEntity(pos) instanceof PotionMugBlockEntity be) {
            be.drink(player);
        }
        super.drink(player, state, level, pos);
    }
}
