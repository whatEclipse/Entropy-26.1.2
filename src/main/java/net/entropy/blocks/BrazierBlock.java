package net.entropy.blocks;

import com.mojang.serialization.MapCodec;
import net.entropy.registry.EntropyBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class BrazierBlock extends IgnitableBlock {
    public static final MapCodec<BrazierBlock> CODEC = simpleCodec(BrazierBlock::new);
    public static final BooleanProperty LEGS = BooleanProperty.create("legs");
    public static final BooleanProperty CHAIN = BooleanProperty.create("chain");

    public BrazierBlock(BlockBehaviour.Properties properties) {
        super(properties, false, true);
        registerDefaultState(defaultBlockState().setValue(LEGS, true).setValue(CHAIN, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    public static int getLuminance(BlockState state) {
        return state.getValue(LIT) ? 15 : 0;
    }

    @Override
    protected SoundEvent getSound(BlockState state) {
        return SoundEvents.CAMPFIRE_CRACKLE;
    }

    @Override
    protected SoundEvent getExtinguishedSound() {
        return SoundEvents.FIRE_EXTINGUISH;
    }

    @Override
    protected List<Vec3> getParticlePositions(BlockState state) {
        return List.of(new Vec3(0.0, 0.5, 0.0));
    }

    @Override
    protected ParticleOptions getSmokeParticle(BlockState state) {
        return ParticleTypes.SMOKE;
    }

    @Override
    protected ParticleOptions getFlameParticle(BlockState state) {
        return ParticleTypes.FLAME;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        if (state == null) {
            return null;
        }
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        return state.setValue(LEGS, canHaveLegs(level, pos)).setValue(CHAIN, canHaveChain(level, pos));
    }

    public boolean canHaveChain(LevelReader level, BlockPos pos) {
        return Block.canSupportCenter(level, pos.above(), Direction.DOWN)
                || level.getBlockState(pos.above()).is(EntropyBlocks.STEEL_BAR);
    }

    public boolean canHaveLegs(LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        BlockState result = super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
        if (direction == Direction.UP) {
            return result.setValue(CHAIN, canHaveChain(level, pos));
        }
        if (direction == Direction.DOWN) {
            return result.setValue(LEGS, canHaveLegs(level, pos));
        }
        return result;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.join(
                Block.box(1, state.getValue(LEGS) ? 0 : 5, 1, 15, 9, 15),
                Block.box(3, 7, 3, 13, 9, 13),
                BooleanOp.ONLY_FIRST);
    }

    protected int getDamage() {
        return 1;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (level instanceof ServerLevel server && state.getValue(LIT) && !entity.fireImmune()
                && entity instanceof LivingEntity && entity.getY() > pos.getY() + 0.4) {
            entity.hurtServer(server, server.damageSources().inFire(), getDamage());
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, WATERLOGGED, LEGS, CHAIN);
    }
}
