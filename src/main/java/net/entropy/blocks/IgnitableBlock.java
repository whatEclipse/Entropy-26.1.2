package net.entropy.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

public abstract class IgnitableBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public final boolean handExtinguishable;

    protected IgnitableBlock(BlockBehaviour.Properties properties, boolean handExtinguishable, boolean litByDefault) {
        super(properties);
        this.handExtinguishable = handExtinguishable;
        registerDefaultState(stateDefinition.any().setValue(LIT, litByDefault).setValue(WATERLOGGED, false));
    }

    public static boolean canBeLit(BlockState state) {
        return state.getBlock() instanceof IgnitableBlock && !state.getValue(LIT) && !state.getValue(WATERLOGGED);
    }

    private static void setLit(LevelAccessor level, BlockState state, BlockPos pos, boolean lit) {
        level.setBlock(pos, state.setValue(LIT, lit), Block.UPDATE_CLIENTS);
    }

    private static boolean isLit(BlockState state) {
        return state.getBlock() instanceof IgnitableBlock && state.getValue(LIT);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {
            for (Vec3 offset : getParticlePositions(state)) {
                spawnParticles(level, state, offset.add(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5), random);
            }
        }
    }

    private void spawnParticles(Level level, BlockState state, Vec3 at, RandomSource random) {
        float f = random.nextFloat();
        if (f < 0.4F) {
            level.addParticle(getSmokeParticle(state), at.x, at.y, at.z, 0.0, 0.07, 0.0);
        }
        level.addParticle(getFlameParticle(state), at.x, at.y, at.z, 0.0, 0.0, 0.0);
        if (f < 0.17F) {
            level.playLocalSound(at.x + 0.5, at.y + 0.5, at.z + 0.5, getSound(state), SoundSource.BLOCKS,
                    1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
        }
    }

    protected abstract SoundEvent getSound(BlockState state);

    protected abstract SoundEvent getExtinguishedSound();

    protected abstract List<Vec3> getParticlePositions(BlockState state);

    protected abstract ParticleOptions getSmokeParticle(BlockState state);

    protected abstract ParticleOptions getFlameParticle(BlockState state);

    private static boolean hasFireAspect(Level level, ItemStack stack) {
        Holder<Enchantment> fireAspect = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.FIRE_ASPECT);
        return EnchantmentHelper.getItemEnchantmentLevel(fireAspect, stack) > 0;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
                                          InteractionHand hand, BlockHitResult hit) {
        RandomSource random = level.getRandom();
        if (isLit(state)) {
            if (stack.is(ItemTags.SHOVELS)) {
                extinguish(player, state, level, pos);
                if (!player.isCreative()) {
                    stack.hurtAndBreak(1, player, hand);
                }
                return InteractionResult.SUCCESS;
            }
        } else if (canBeLit(state)) {
            if (stack.is(Items.FIRE_CHARGE)) {
                setLit(level, state, pos, true);
                level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F,
                        (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            if (stack.is(Items.FLINT_AND_STEEL) || hasFireAspect(level, stack)) {
                setLit(level, state, pos, true);
                level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F,
                        random.nextFloat() * 0.4F + 0.8F);
                if (!player.isCreative()) {
                    stack.hurtAndBreak(1, player, hand);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (handExtinguishable && isLit(state)) {
            extinguish(player, state, level, pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.getValue(WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
            BlockState wet = state.setValue(WATERLOGGED, true);
            if (state.getValue(LIT)) {
                extinguish(null, wet, level, pos);
            } else {
                level.setBlock(pos, wet, Block.UPDATE_ALL);
            }
            level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            return true;
        }
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
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!level.isClientSide() && projectile.isOnFire() && canBeLit(state)) {
            setLit(level, state, hit.getBlockPos(), true);
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());
        if (fluid.getType() == Fluids.WATER) {
            return defaultBlockState().setValue(WATERLOGGED, true).setValue(LIT, false);
        }
        return defaultBlockState();
    }

    public void extinguish(@Nullable Player player, BlockState state, LevelAccessor level, BlockPos pos) {
        setLit(level, state, pos, false);
        level.playSound(null, pos, getExtinguishedSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, WATERLOGGED);
    }
}
