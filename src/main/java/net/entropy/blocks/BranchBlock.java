package net.entropy.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;


public class BranchBlock extends PipeBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<BranchBlock> CODEC = simpleCodec(BranchBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final @Nullable Supplier<Block> stripped;

    public BranchBlock(BlockBehaviour.Properties properties) {
        this(properties, null);
    }

    public BranchBlock(BlockBehaviour.Properties properties, @Nullable Supplier<Block> stripped) {
        super(8.0F, properties);
        this.stripped = stripped;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false)
                .setValue(WATERLOGGED, false));
    }


    @Override
    protected MapCodec<? extends PipeBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
                                          InteractionHand hand, BlockHitResult hit) {
        if (stack.is(ItemTags.AXES) && stripped != null) {
            level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!level.isClientSide()) {
                level.setBlock(pos, stripped.get().withPropertiesOf(state), Block.UPDATE_ALL);
                stack.hurtAndBreak(1, player, hand);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    private static BlockState getConnectedState(BlockState state, BlockPos pos, LevelReader level) {
        for (Direction d : Direction.values()) {
            BlockState neighbor = level.getBlockState(pos.relative(d));
            if (neighbor.getBlock() instanceof BranchBlock && neighbor.getValue(PROPERTY_BY_DIRECTION.get(d.getOpposite()))) {
                state = state.setValue(PROPERTY_BY_DIRECTION.get(d), true);
            }
        }
        return state;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return getConnectedState(state, pos, level);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        Level level = ctx.getLevel();
        FluidState fluid = level.getFluidState(pos);
        return getConnectedState(defaultBlockState(), pos, level)
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER)
                .setValue(PROPERTY_BY_DIRECTION.get(ctx.getClickedFace().getOpposite()), true);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, WATERLOGGED);
    }
}
