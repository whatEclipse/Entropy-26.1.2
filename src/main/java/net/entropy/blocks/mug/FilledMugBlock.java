package net.entropy.blocks.mug;

import net.entropy.registry.EntropyBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

public abstract class FilledMugBlock extends MugBlock {
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 1, 3);

    protected FilledMugBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(LEVEL, 3));
    }

    @Override
    protected abstract MapCodec<? extends HorizontalDirectionalBlock> codec();

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
                                          InteractionHand hand, BlockHitResult hit) {
        if (canFill(state, stack)) {
            if (!level.isClientSide()) {
                fill(state, level, pos, player, hand, stack);
                play(level, pos, getFillSound());
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide()) {
            drink(player, state, level, pos);
            play(level, pos, getDrinkSound());
        }
        return InteractionResult.SUCCESS;
    }

    protected SoundEvent getFillSound() {
        return SoundEvents.BOTTLE_EMPTY;
    }

    protected SoundEvent getDrinkSound() {
        return SoundEvents.GENERIC_DRINK.value();
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        return state.getValue(LEVEL);
    }

    protected boolean canFill(BlockState state, ItemStack stack) {
        return state.getValue(LEVEL) < 3;
    }

    protected void fill(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        setFilled(this, 3, state, level, pos);
    }

    protected void drink(Player player, BlockState state, Level level, BlockPos pos) {
        int fill = state.getValue(LEVEL);
        if (fill == 1) {
            level.setBlock(pos, EntropyBlocks.MUG.withPropertiesOf(state), Block.UPDATE_ALL);
        } else {
            level.setBlock(pos, state.setValue(LEVEL, fill - 1), Block.UPDATE_ALL);
        }
    }

    protected static void give(Player player, InteractionHand hand, ItemStack stack, ItemStack result) {
        player.setItemInHand(hand, net.minecraft.world.item.ItemUtils.createFilledResult(stack, player, result));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LEVEL);
    }
}
