package net.entropy.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class SteelBarBlock extends Block implements SimpleWaterloggedBlock {
    public static final MapCodec<SteelBarBlock> CODEC = simpleCodec(SteelBarBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty CHAIN = BooleanProperty.create("chain");
    public static final BooleanProperty TOP_CAP = BooleanProperty.create("top_cap");
    public static final BooleanProperty BOTTOM_CAP = BooleanProperty.create("bottom_cap");

    private static final VoxelShape Y_TOP = Block.box(6, 14, 6, 10, 16, 10);
    private static final VoxelShape Z_TOP = Block.box(6, 6, 14, 10, 10, 16);
    private static final VoxelShape X_TOP = Block.box(14, 6, 6, 16, 10, 10);
    private static final VoxelShape Y_BOTTOM = Block.box(6, 0, 6, 10, 2, 10);
    private static final VoxelShape Z_BOTTOM = Block.box(6, 6, 0, 10, 10, 2);
    private static final VoxelShape X_BOTTOM = Block.box(0, 6, 6, 2, 10, 10);
    private static final VoxelShape Y_SHAPE = Block.box(7, 0, 7, 9, 16, 9);
    private static final VoxelShape Z_SHAPE = Block.box(7, 7, 0, 9, 9, 16);
    private static final VoxelShape X_SHAPE = Block.box(0, 7, 7, 16, 9, 9);

    public SteelBarBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(AXIS, Direction.Axis.Y).setValue(WATERLOGGED, false)
                .setValue(CHAIN, false).setValue(TOP_CAP, true).setValue(BOTTOM_CAP, true));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    private static VoxelShape shape(VoxelShape base, VoxelShape top, VoxelShape bottom, boolean hasTop, boolean hasBottom) {
        VoxelShape result = base;
        if (hasTop) {
            result = Shapes.or(result, top);
        }
        if (hasBottom) {
            result = Shapes.or(result, bottom);
        }
        return result;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        boolean top = state.getValue(TOP_CAP);
        boolean bottom = state.getValue(BOTTOM_CAP);
        return switch (state.getValue(AXIS)) {
            case X -> shape(X_SHAPE, X_TOP, X_BOTTOM, top, bottom);
            case Y -> shape(Y_SHAPE, Y_TOP, Y_BOTTOM, top, bottom);
            case Z -> shape(Z_SHAPE, Z_TOP, Z_BOTTOM, top, bottom);
        };
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());
        return defaultBlockState().setValue(WATERLOGGED, fluid.getType() == Fluids.WATER)
                .setValue(AXIS, ctx.getClickedFace().getAxis());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, WATERLOGGED, CHAIN, TOP_CAP, BOTTOM_CAP);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (direction == Direction.DOWN) {
            boolean hangs = (neighborState.getBlock() instanceof LanternBlock && neighborState.getValue(LanternBlock.HANGING))
                    || (neighborState.getBlock() instanceof ChainBlock && neighborState.getValue(ChainBlock.AXIS) == Direction.Axis.Y)
                    || neighborState.getBlock() instanceof BrazierBlock;
            state = state.setValue(CHAIN, hangs && state.getValue(AXIS).isHorizontal());
        }
        if (state.getValue(AXIS) == direction.getAxis()) {
            boolean connected = neighborState.getBlock() instanceof SteelBarBlock
                    && neighborState.getValue(AXIS) == state.getValue(AXIS);
            if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                state = state.setValue(TOP_CAP, !connected);
            } else {
                state = state.setValue(BOTTOM_CAP, !connected);
            }
        }
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
    }
}
