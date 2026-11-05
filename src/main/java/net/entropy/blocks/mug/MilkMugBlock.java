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

public class MilkMugBlock extends FilledMugBlock {
    public static final MapCodec<MilkMugBlock> CODEC = simpleCodec(MilkMugBlock::new);

    public MilkMugBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean canFill(BlockState state, ItemStack stack) {
        return (stack.is(Items.MILK_BUCKET) || stack.is(Items.POTION)) && super.canFill(state, stack);
    }

    @Override
    protected void fill(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        if (stack.is(Items.MILK_BUCKET)) {
            super.fill(state, level, pos, player, hand, stack);
            give(player, hand, stack, new ItemStack(Items.BUCKET));
        } else {
            int fill = state.getValue(LEVEL);
            PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
            setFilled(EntropyBlocks.PILK_MUG, fill + 1, state, level, pos);
            if (contents != null && level.getBlockEntity(pos) instanceof PotionMugBlockEntity be) {
                be.addEffects(contents.getAllEffects());
            }
            give(player, hand, stack, new ItemStack(Items.GLASS_BOTTLE));
        }
    }

    @Override
    protected SoundEvent getDrinkSound() {
        return SoundEvents.WANDERING_TRADER_DRINK_MILK;
    }

    @Override
    protected void drink(Player player, BlockState state, Level level, BlockPos pos) {
        super.drink(player, state, level, pos);
        player.removeAllEffects();
    }
}
