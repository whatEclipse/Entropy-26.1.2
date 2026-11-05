package net.entropy.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

/**
 * A 1/16 thin carpet-like layer (moss, snow) that hugs the shape of the block below it.
 */
public class LayerBlock extends Block {
    public static final MapCodec<LayerBlock> CODEC = simpleCodec(LayerBlock::new);
    public static final EnumProperty<LayerShape> SHAPE = EnumProperty.create("shape", LayerShape.class);
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    public LayerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(SHAPE, LayerShape.SLAB).setValue(UP, true)
                .setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, UP, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        if (state == null) {
            return null;
        }
        BlockPos belowPos = ctx.getClickedPos().below();
        return getUpdatedBlockState(state, ctx.getLevel().getBlockState(belowPos), ctx.getLevel(), belowPos);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        BlockPos belowPos = pos.below();
        BlockState updated = getUpdatedBlockState(state, level.getBlockState(belowPos), level, belowPos);
        return updated != null ? updated : Blocks.AIR.defaultBlockState();
    }

    private static boolean quadrantIsFull(VoxelShape shape, double x0, double z0) {
        return Shapes.joinIsNotEmpty(shape, Shapes.box(x0, 0.9, z0, x0 + 0.5, 1.0, z0 + 0.5), BooleanOp.AND);
    }

    private static BlockState horizontal(BlockState state, Direction facing, boolean invert) {
        // The layer extends toward the side the open door/trapdoor/gate leaf is on.
        return state
                .setValue(NORTH, facing == Direction.SOUTH)
                .setValue(EAST, facing == Direction.WEST)
                .setValue(SOUTH, facing == Direction.NORTH)
                .setValue(WEST, facing == Direction.EAST);
    }

    public @Nullable BlockState getUpdatedBlockState(BlockState state, BlockState below, LevelReader level, BlockPos belowPos) {
        Block block = below.getBlock();
        if (block instanceof SlabBlock) {
            return state.setValue(SHAPE, LayerShape.SLAB).setValue(UP, below.getValue(SlabBlock.TYPE) != SlabType.BOTTOM);
        }
        if (block instanceof StairBlock) {
            if (below.getValue(StairBlock.HALF) == Half.TOP) {
                return state.setValue(SHAPE, LayerShape.SLAB).setValue(UP, true);
            }
            // Quadrants (see getShape): WEST = north-west, NORTH = north-east, EAST = south-east, SOUTH = south-west.
            VoxelShape shape = below.getCollisionShape(level, belowPos);
            return state.setValue(SHAPE, LayerShape.STAIR)
                    .setValue(WEST, quadrantIsFull(shape, 0.0, 0.0))
                    .setValue(NORTH, quadrantIsFull(shape, 0.5, 0.0))
                    .setValue(EAST, quadrantIsFull(shape, 0.5, 0.5))
                    .setValue(SOUTH, quadrantIsFull(shape, 0.0, 0.5));
        }
        if (block instanceof FenceBlock || block instanceof IronBarsBlock) {
            return state.setValue(SHAPE, block instanceof FenceBlock ? LayerShape.FENCE : LayerShape.PANE)
                    .setValue(NORTH, below.getValue(NORTH)).setValue(EAST, below.getValue(EAST))
                    .setValue(SOUTH, below.getValue(SOUTH)).setValue(WEST, below.getValue(WEST));
        }
        if (block instanceof WallBlock) {
            return state.setValue(SHAPE, LayerShape.WALL)
                    .setValue(NORTH, below.getValue(WallBlock.NORTH) != WallSide.NONE)
                    .setValue(EAST, below.getValue(WallBlock.EAST) != WallSide.NONE)
                    .setValue(SOUTH, below.getValue(WallBlock.SOUTH) != WallSide.NONE)
                    .setValue(WEST, below.getValue(WallBlock.WEST) != WallSide.NONE)
                    .setValue(UP, below.getValue(WallBlock.UP));
        }
        if (block instanceof TrapDoorBlock) {
            Direction facing = below.getValue(TrapDoorBlock.FACING);
            if (below.getValue(TrapDoorBlock.OPEN)) {
                return horizontal(state, facing, false).setValue(SHAPE, LayerShape.DOOR);
            }
            return state.setValue(SHAPE, LayerShape.TRAPDOOR).setValue(UP, below.getValue(TrapDoorBlock.HALF) == Half.TOP);
        }
        if (block instanceof DoorBlock) {
            Direction d = below.getValue(DoorBlock.FACING);
            if (below.getValue(DoorBlock.OPEN)) {
                d = below.getValue(DoorBlock.HINGE) == DoorHingeSide.LEFT ? d.getClockWise() : d.getCounterClockWise();
            }
            return horizontal(state, d, false).setValue(SHAPE, LayerShape.DOOR);
        }
        if (block instanceof FenceGateBlock) {
            return horizontal(state, below.getValue(FenceGateBlock.FACING), false)
                    .setValue(UP, !below.getValue(FenceGateBlock.IN_WALL))
                    .setValue(SHAPE, below.getValue(FenceGateBlock.OPEN) ? LayerShape.OPEN_FENCE_GATE : LayerShape.FENCE_GATE);
        }
        if (below.isFaceSturdy(level, belowPos, Direction.UP) || block instanceof LeavesBlock) {
            return state.setValue(SHAPE, LayerShape.SLAB).setValue(UP, true);
        }
        return null;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState below = level.getBlockState(belowPos);
        if (below.isFaceSturdy(level, belowPos, Direction.UP) || below.getBlock() instanceof LeavesBlock) {
            return true;
        }
        Block b = below.getBlock();
        return b instanceof SlabBlock || b instanceof FenceBlock || b instanceof WallBlock || b instanceof StairBlock
                || b instanceof TrapDoorBlock || b instanceof DoorBlock || b instanceof FenceGateBlock || b instanceof IronBarsBlock;
    }

    private static VoxelShape flat(double x0, double y0, double z0, double x1, double y1, double z1, boolean on) {
        return on ? Block.box(x0, y0, z0, x1, y1, z1) : Shapes.empty();
    }

    private static VoxelShape quarter(boolean high, double x0, double z0) {
        return high ? Block.box(x0, 0, z0, x0 + 8, 1, z0 + 8) : Block.box(x0, -8, z0, x0 + 8, -7, z0 + 8);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        boolean up = state.getValue(UP);
        boolean n = state.getValue(NORTH);
        boolean e = state.getValue(EAST);
        boolean s = state.getValue(SOUTH);
        boolean w = state.getValue(WEST);
        return switch (state.getValue(SHAPE)) {
            case SLAB -> up ? Block.box(0, 0, 0, 16, 1, 16) : Block.box(0, -8, 0, 16, -7, 16);
            case STAIR -> Shapes.or(quarter(w, 0, 0), quarter(n, 8, 0), quarter(e, 8, 8), quarter(s, 0, 8));
            case TRAPDOOR -> up ? Block.box(0, 0, 0, 16, 1, 16) : Block.box(0, -13, 0, 16, -12, 16);
            case DOOR -> {
                if (n) {
                    yield Block.box(0, 0, 0, 16, 1, 3);
                }
                if (e) {
                    yield Block.box(13, 0, 0, 16, 1, 16);
                }
                if (s) {
                    yield Block.box(0, 0, 13, 16, 1, 16);
                }
                yield Block.box(0, 0, 0, 3, 1, 16);
            }
            case WALL -> Shapes.or(flat(4, 0, 4, 12, 1, 12, up),
                    flat(0, -2, 5, 11, -1, 11, w), flat(11, -2, 5, 16, -1, 11, e),
                    flat(5, -2, 0, 11, -1, 11, n), flat(5, -2, 11, 11, -1, 16, s));
            case FENCE -> Shapes.or(Block.box(6, 0, 6, 10, 1, 10),
                    flat(0, 0, 6, 10, 1, 10, w), flat(10, 0, 6, 16, 1, 10, e),
                    flat(6, 0, 0, 10, 1, 10, n), flat(6, 0, 10, 10, 1, 16, s));
            case PANE -> Shapes.or(Block.box(7, 0, 7, 9, 1, 9),
                    flat(0, 0, 7, 9, 1, 9, w), flat(9, 0, 7, 16, 1, 9, e),
                    flat(7, 0, 0, 9, 1, 9, n), flat(7, 0, 9, 9, 1, 16, s));
            case FENCE_GATE, OPEN_FENCE_GATE -> {
                double y0 = up ? 0 : -3;
                double y1 = up ? 1 : -2;
                if (n || s) {
                    yield Block.box(0, y0, 6, 16, y1, 10);
                }
                yield Block.box(6, y0, 0, 10, y1, 16);
            }
        };
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return state.getFluidState().isEmpty();
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return true;
    }

    public enum LayerShape implements StringRepresentable {
        SLAB("slab"), STAIR("stair"), WALL("wall"), FENCE("fence"), TRAPDOOR("trapdoor"),
        DOOR("door"), FENCE_GATE("fence_gate"), OPEN_FENCE_GATE("open_fence_gate"), PANE("pane");

        private final String name;

        LayerShape(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
