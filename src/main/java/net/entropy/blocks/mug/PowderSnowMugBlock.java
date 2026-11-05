package net.entropy.blocks.mug;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.entropy.blockentities.PotionMugBlockEntity;
import net.entropy.registry.EntropyBlocks;

public class PowderSnowMugBlock extends FilledMugBlock {
    public static final MapCodec<PowderSnowMugBlock> CODEC = simpleCodec(PowderSnowMugBlock::new);

    public PowderSnowMugBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean canFill(BlockState state, ItemStack stack) {
        return stack.is(Items.POWDER_SNOW_BUCKET) && super.canFill(state, stack);
    }

    @Override
    protected void drink(Player player, BlockState state, Level level, BlockPos pos) {
        super.drink(player, state, level, pos);
        player.setTicksFrozen(300);
        if (level instanceof ServerLevel server) {
            player.hurtServer(server, server.damageSources().freeze(), 1.0F);
        }
    }

    @Override
    protected void fill(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        super.fill(state, level, pos, player, hand, stack);
        give(player, hand, stack, new ItemStack(Items.BUCKET));
    }

    @Override
    protected SoundEvent getFillSound() {
        return SoundEvents.BUCKET_EMPTY_POWDER_SNOW;
    }

    @Override
    protected SoundEvent getDrinkSound() {
        return SoundEvents.POWDER_SNOW_BREAK;
    }
}
