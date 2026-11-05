package net.entropy.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

/** Emits a redstone signal from its sides while the two blocks on its axis ends are the exact same state. */
public class EquivalenceDetectorBlock extends RotatedPillarBlock {
    public static final MapCodec<EquivalenceDetectorBlock> CODEC = simpleCodec(EquivalenceDetectorBlock::new);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public EquivalenceDetectorBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }

    @Override
    public MapCodec<RotatedPillarBlock> codec() {
        // RotatedPillarBlock#codec is declared with its own type; the cast keeps this subclass's codec.
        @SuppressWarnings({"unchecked", "rawtypes"})
        MapCodec<RotatedPillarBlock> c = (MapCodec) CODEC;
        return c;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (direction.getAxis() == state.getValue(AXIS)) {
            ticks.scheduleTick(pos, this, 1);
        }
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, state.getValue(AXIS));
        BlockState a = level.getBlockState(pos.relative(positive));
        BlockState b = level.getBlockState(pos.relative(positive.getOpposite()));
        // BlockStates are interned, so identity means "same block and same properties".
        boolean equal = a == b;
        if (equal != state.getValue(POWERED)) {
            level.setBlock(pos, state.setValue(POWERED, equal), Block.UPDATE_ALL);
        }
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        level.scheduleTick(pos, this, 1);
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) && direction.getAxis() != state.getValue(AXIS) ? 15 : 0;
    }
}
