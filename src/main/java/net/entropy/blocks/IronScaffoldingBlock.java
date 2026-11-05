package net.entropy.blocks;

import com.mojang.serialization.MapCodec;
import net.entropy.registry.EntropyBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IronScaffoldingBlock extends Block implements SimpleWaterloggedBlock {
    public static final MapCodec<IronScaffoldingBlock> CODEC = simpleCodec(IronScaffoldingBlock::new);
    public static final int MAX_DISTANCE = 21;
    public static final IntegerProperty DISTANCE = IntegerProperty.create("distance", 0, MAX_DISTANCE);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;

    private static final VoxelShape COLLISION_SHAPE = Block.box(0, 0, 0, 16, 2, 16);
    private static final VoxelShape OUTLINE_SHAPE = Shapes.block().move(0.0, -1.0, 0.0);
    private static final VoxelShape NORMAL_OUTLINE_SHAPE = Shapes.or(
            Block.box(0, 12, 0, 16, 16, 16),
            Block.box(0, 0, 0, 2, 16, 2), Block.box(14, 0, 0, 16, 16, 2),
            Block.box(0, 0, 14, 2, 16, 16), Block.box(14, 0, 14, 16, 16, 16));
    private static final VoxelShape BOTTOM_OUTLINE_SHAPE = Shapes.or(
            COLLISION_SHAPE, NORMAL_OUTLINE_SHAPE,
            Block.box(0, 0, 0, 2, 2, 16), Block.box(14, 0, 0, 16, 2, 16),
            Block.box(0, 0, 14, 16, 2, 16), Block.box(0, 0, 0, 16, 2, 2));

    public IronScaffoldingBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(DISTANCE, MAX_DISTANCE).setValue(WATERLOGGED, false).setValue(BOTTOM, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    public static int calculateDistance(BlockGetter level, BlockPos pos) {
        BlockPos.MutableBlockPos mutable = pos.mutable().move(Direction.DOWN);
        BlockState below = level.getBlockState(mutable);
        int distance = MAX_DISTANCE;
        if (below.is(EntropyBlocks.IRON_SCAFFOLDING)) {
            distance = below.getValue(DISTANCE);
        } else if (below.isFaceSturdy(level, mutable, Direction.UP)) {
            return 0;
        }
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState side = level.getBlockState(mutable.setWithOffset(pos, direction));
            if (side.is(EntropyBlocks.IRON_SCAFFOLDING)) {
                distance = Math.min(distance, side.getValue(DISTANCE) + 1);
                if (distance == 1) {
                    break;
                }
            }
        }
        return distance;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, WATERLOGGED, BOTTOM);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (!context.isHoldingItem(state.getBlock().asItem())) {
            return state.getValue(BOTTOM) ? BOTTOM_OUTLINE_SHAPE : NORMAL_OUTLINE_SHAPE;
        }
        return Shapes.block();
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return context.getItemInHand().is(this.asItem());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        Level level = ctx.getLevel();
        int distance = calculateDistance(level, pos);
        return defaultBlockState()
                .setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER)
                .setValue(DISTANCE, distance)
                .setValue(BOTTOM, shouldBeBottom(level, pos, distance));
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide()) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (!level.isClientSide()) {
            ticks.scheduleTick(pos, this, 1);
        }
        return state;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int distance = calculateDistance(level, pos);
        BlockState updated = state.setValue(DISTANCE, distance).setValue(BOTTOM, shouldBeBottom(level, pos, distance));
        if (updated.getValue(DISTANCE) == MAX_DISTANCE) {
            if (state.getValue(DISTANCE) == MAX_DISTANCE) {
                FallingBlockEntity.fall(level, pos, updated.setValue(WATERLOGGED, false));
            } else {
                level.destroyBlock(pos, true);
            }
        } else if (state != updated) {
            level.setBlock(pos, updated, Block.UPDATE_ALL);
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return calculateDistance(level, pos) < MAX_DISTANCE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (!context.isAbove(Shapes.block(), pos, true) || context.isDescending()) {
            if (state.getValue(DISTANCE) != 0 && state.getValue(BOTTOM) && context.isAbove(OUTLINE_SHAPE, pos, true)) {
                return COLLISION_SHAPE;
            }
            return Shapes.empty();
        }
        return NORMAL_OUTLINE_SHAPE;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    private boolean shouldBeBottom(BlockGetter level, BlockPos pos, int distance) {
        return distance > 0 && !level.getBlockState(pos.below()).is(this);
    }
}
