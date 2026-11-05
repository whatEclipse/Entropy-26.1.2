package net.entropy.blocks.mug;

import com.mojang.serialization.MapCodec;
import net.entropy.blockentities.PotionMugBlockEntity;
import net.entropy.registry.EntropyBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class MugBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<MugBlock> CODEC = simpleCodec(MugBlock::new);
    private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 10, 12);

    public MugBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    protected static void play(Level level, BlockPos pos, SoundEvent sound) {
        if (!level.isClientSide()) {
            level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    private InteractionResult swap(ItemStack stack, Player player, InteractionHand hand, ItemStack result) {
        player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, result));
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
                                          InteractionHand hand, BlockHitResult hit) {
        ItemStack bucket = new ItemStack(Items.BUCKET);
        ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
        if (stack.is(Items.LAVA_BUCKET)) {
            if (!level.isClientSide()) {
                setFilled(EntropyBlocks.LAVA_MUG, 3, state, level, pos);
                play(level, pos, SoundEvents.BUCKET_EMPTY_LAVA);
            }
            return swap(stack, player, hand, bucket);
        }
        if (stack.is(Items.MILK_BUCKET)) {
            if (!level.isClientSide()) {
                setFilled(EntropyBlocks.MILK_MUG, 3, state, level, pos);
                play(level, pos, SoundEvents.BOTTLE_EMPTY);
            }
            return swap(stack, player, hand, bucket);
        }
        if (stack.has(DataComponents.POTION_CONTENTS) && stack.is(Items.POTION)) {
            if (!level.isClientSide()) {
                setFilled(EntropyBlocks.POTION_MUG, 1, state, level, pos);
                if (level.getBlockEntity(pos) instanceof PotionMugBlockEntity be) {
                    PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
                    be.addEffects(contents.getAllEffects());
                }
                play(level, pos, SoundEvents.BOTTLE_EMPTY);
            }
            return swap(stack, player, hand, bottle);
        }
        if (stack.is(Items.WATER_BUCKET)) {
            if (!level.isClientSide()) {
                setFilled(EntropyBlocks.POTION_MUG, 3, state, level, pos);
                play(level, pos, SoundEvents.BUCKET_EMPTY);
            }
            return swap(stack, player, hand, bucket);
        }
        if (stack.is(Items.POWDER_SNOW_BUCKET)) {
            if (!level.isClientSide()) {
                setFilled(EntropyBlocks.POWDER_SNOW_MUG, 3, state, level, pos);
                play(level, pos, SoundEvents.BUCKET_EMPTY_POWDER_SNOW);
            }
            return swap(stack, player, hand, bucket);
        }
        if (stack.is(Items.HONEY_BOTTLE)) {
            if (!level.isClientSide()) {
                setFilled(EntropyBlocks.HONEY_MUG, 1, state, level, pos);
                play(level, pos, SoundEvents.BOTTLE_EMPTY);
            }
            return swap(stack, player, hand, bottle);
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    protected void setFilled(Block block, int fill, BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, block.withPropertiesOf(state).setValue(FilledMugBlock.LEVEL, fill), Block.UPDATE_ALL);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (direction == Direction.DOWN && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return Block.canSupportCenter(level, pos.below(), Direction.UP);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    /** Filled mugs have no item of their own; pick-block gives the empty mug. No @Override: signature differs across versions. */
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(EntropyBlocks.MUG.asItem());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
