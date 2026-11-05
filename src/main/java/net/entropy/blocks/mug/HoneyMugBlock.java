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

public class HoneyMugBlock extends FilledMugBlock {
    public static final MapCodec<HoneyMugBlock> CODEC = simpleCodec(HoneyMugBlock::new);

    public HoneyMugBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean canFill(BlockState state, ItemStack stack) {
        return stack.is(Items.HONEY_BOTTLE) && super.canFill(state, stack);
    }

    @Override
    protected SoundEvent getDrinkSound() {
        return SoundEvents.HONEY_DRINK.value();
    }

    @Override
    protected void drink(Player player, BlockState state, Level level, BlockPos pos) {
        super.drink(player, state, level, pos);
        player.getFoodData().eat(Foods.HONEY_BOTTLE);
    }

    @Override
    protected void fill(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        setFilled(this, state.getValue(LEVEL) + 1, state, level, pos);
        give(player, hand, stack, new ItemStack(Items.GLASS_BOTTLE));
    }
}
