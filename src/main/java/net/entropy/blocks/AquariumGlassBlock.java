package net.entropy.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class AquariumGlassBlock extends Block implements SimpleWaterloggedBlock {
    public static final MapCodec<AquariumGlassBlock> CODEC = simpleCodec(AquariumGlassBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    private static final VoxelShape DOWN_SHAPE = Block.box(0, 15, 0, 16, 16, 16);
    private static final VoxelShape UP_SHAPE = Block.box(0, 0, 0, 16, 1, 16);
    private static final VoxelShape EAST_SHAPE = Block.box(0, 0, 0, 1, 16, 16);
    private static final VoxelShape WEST_SHAPE = Block.box(15, 0, 0, 16, 16, 16);
    private static final VoxelShape SOUTH_SHAPE = Block.box(0, 0, 0, 16, 16, 1);
    private static final VoxelShape NORTH_SHAPE = Block.box(0, 0, 15, 16, 16, 16);
    private static final VoxelShape THICK_DOWN_SHAPE = Block.box(0, 12, 0, 16, 16, 16);
    private static final VoxelShape THICK_UP_SHAPE = Block.box(0, 0, 0, 16, 4, 16);
    private static final VoxelShape THICK_EAST_SHAPE = Block.box(0, 0, 0, 4, 16, 16);
    private static final VoxelShape THICK_WEST_SHAPE = Block.box(12, 0, 0, 16, 16, 16);
    private static final VoxelShape THICK_SOUTH_SHAPE = Block.box(0, 0, 0, 16, 16, 4);
    private static final VoxelShape THICK_NORTH_SHAPE = Block.box(0, 0, 12, 16, 16, 16);

    public AquariumGlassBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext ctx && ctx.getEntity() instanceof Player player
                && (player.getMainHandItem().is(this.asItem()) || player.getOffhandItem().is(this.asItem()))) {
            return switch (state.getValue(FACING)) {
                case NORTH -> THICK_NORTH_SHAPE;
                case SOUTH -> THICK_SOUTH_SHAPE;
                case UP -> THICK_UP_SHAPE;
                case DOWN -> THICK_DOWN_SHAPE;
                case EAST -> THICK_EAST_SHAPE;
                case WEST -> THICK_WEST_SHAPE;
            };
        }
        return getCollisionShape(state, level, pos, context);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case UP -> UP_SHAPE;
            case DOWN -> DOWN_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
        };
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction side = ctx.getClickedFace();
        BlockPos pos = ctx.getClickedPos();
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        BlockState clickedState = level.getBlockState(pos.relative(side.getOpposite()));
        boolean sneaking = player != null && player.isShiftKeyDown();
        if (sneaking && !player.isUnderWater() && level.getBlockState(pos).is(Blocks.WATER)
                && level.getFluidState(pos).is(Fluids.WATER)) {
            return defaultBlockState().setValue(WATERLOGGED, true).setValue(FACING, side.getOpposite());
        }
        if (!sneaking && clickedState.getBlock() instanceof AquariumGlassBlock
                && clickedState.getValue(FACING).getAxis() != side.getAxis()) {
            side = clickedState.getValue(FACING);
        }
        FluidState fluid = level.getFluidState(pos);
        return defaultBlockState().setValue(WATERLOGGED, fluid.getType() == Fluids.WATER).setValue(FACING, side);
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state) {
        return Shapes.empty();
    }

    @Override
    protected boolean skipRendering(BlockState state, BlockState neighbor, Direction direction) {
        if (neighbor.is(this)) {
            Direction nf = neighbor.getValue(FACING);
            if (nf == direction) {
                return state.getValue(FACING) == direction.getOpposite();
            }
            if (nf == direction.getOpposite()) {
                return state.getValue(FACING) == direction;
            }
            if (nf == state.getValue(FACING)) {
                return true;
            }
        }
        return super.skipRendering(state, neighbor, direction);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }
}
