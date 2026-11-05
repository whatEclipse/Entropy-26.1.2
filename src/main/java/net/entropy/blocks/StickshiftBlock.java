package net.entropy.blocks;

import com.mojang.serialization.MapCodec;
import net.entropy.sounds.EntropySounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/** A wall/floor/ceiling mounted lever-like gear stick with 7 redstone power levels. */
public class StickshiftBlock extends FaceAttachedHorizontalDirectionalBlock {
    public static final MapCodec<StickshiftBlock> CODEC = simpleCodec(StickshiftBlock::new);
    public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 6);
    public static final BooleanProperty BAD_TO_THE_BONE = BooleanProperty.create("bad_to_the_bone");

    private static final VoxelShape NORTH_WALL_SHAPE = Block.box(5, 4, 10, 11, 12, 16);
    private static final VoxelShape SOUTH_WALL_SHAPE = Block.box(5, 4, 0, 11, 12, 6);
    private static final VoxelShape WEST_WALL_SHAPE = Block.box(10, 4, 5, 16, 12, 11);
    private static final VoxelShape EAST_WALL_SHAPE = Block.box(0, 4, 5, 6, 12, 11);
    private static final VoxelShape FLOOR_Z_AXIS_SHAPE = Block.box(5, 0, 4, 11, 6, 12);
    private static final VoxelShape FLOOR_X_AXIS_SHAPE = Block.box(4, 0, 5, 12, 6, 11);
    private static final VoxelShape CEILING_Z_AXIS_SHAPE = Block.box(5, 10, 4, 11, 16, 12);
    private static final VoxelShape CEILING_X_AXIS_SHAPE = Block.box(4, 10, 5, 12, 16, 11);

    public StickshiftBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(BAD_TO_THE_BONE, false).setValue(FACING, Direction.NORTH)
                .setValue(FACE, AttachFace.WALL).setValue(POWER, 0));
    }

    @Override
    protected MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    private static void spawnParticles(BlockState state, LevelAccessor level, BlockPos pos, float alpha) {
        Direction d1 = state.getValue(FACING).getOpposite();
        Direction d2 = getConnectedDirection(state).getOpposite();
        level.addParticle(new DustParticleOptions(0xFF0000, alpha),
                pos.getX() + 0.5 + 0.1 * d1.getStepX() + 0.2 * d2.getStepX(),
                pos.getY() + 0.5 + 0.1 * d1.getStepY() + 0.2 * d2.getStepY(),
                pos.getZ() + 0.5 + 0.1 * d1.getStepZ() + 0.2 * d2.getStepZ(),
                0.0, 0.0, 0.0);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return switch (state.getValue(FACE)) {
            case FLOOR -> facing.getAxis() == Direction.Axis.X ? FLOOR_X_AXIS_SHAPE : FLOOR_Z_AXIS_SHAPE;
            case WALL -> switch (facing) {
                case EAST -> EAST_WALL_SHAPE;
                case WEST -> WEST_WALL_SHAPE;
                case SOUTH -> SOUTH_WALL_SHAPE;
                default -> NORTH_WALL_SHAPE;
            };
            case CEILING -> facing.getAxis() == Direction.Axis.X ? CEILING_X_AXIS_SHAPE : CEILING_Z_AXIS_SHAPE;
        };
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return getConnectedDirection(state) == direction ? getSignal(state, level, pos, direction) : 0;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(POWER) * 2;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < 0.05F * state.getValue(POWER)) {
            spawnParticles(state, level, pos, 0.5F);
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
                                          InteractionHand hand, BlockHitResult hit) {
        if (stack.is(Items.SKELETON_SKULL) && !state.getValue(BAD_TO_THE_BONE)) {
            if (level.isClientSide()) {
                return InteractionResult.SUCCESS;
            }
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            level.setBlock(pos, state.setValue(BAD_TO_THE_BONE, true), Block.UPDATE_ALL);
            level.playSound(null, pos, EntropySounds.STICKSHIFT_BAD_TO_THE_BONE, SoundSource.BLOCKS, 1.0F,
                    0.8F + state.getValue(POWER) * 0.2F);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        boolean sneaking = player.isShiftKeyDown();
        int power = state.getValue(POWER);
        if ((sneaking && power == 0) || (!sneaking && power == 6)) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        cyclePower(state, level, pos, sneaking);
        level.playSound(null, pos, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 0.3F, sneaking ? 0.6F : 0.5F);
        level.gameEvent(player, sneaking ? GameEvent.BLOCK_DEACTIVATE : GameEvent.BLOCK_ACTIVATE, pos);
        return InteractionResult.CONSUME;
    }

    public void cyclePower(BlockState state, Level level, BlockPos pos, boolean sneaking) {
        int power = Math.max(Math.min(state.getValue(POWER) + (sneaking ? -1 : 1), 6), 0);
        state = state.setValue(POWER, power);
        level.setBlock(pos, state, Block.UPDATE_ALL);
        updateNeighbors(state, level, pos);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean moved) {
        if (!moved && state.getValue(POWER) > 0) {
            updateNeighbors(state, level, pos);
        }
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    private void updateNeighbors(BlockState state, Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
        level.updateNeighborsAt(pos.relative(getConnectedDirection(state).getOpposite()), this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE, POWER, BAD_TO_THE_BONE);
    }
}
