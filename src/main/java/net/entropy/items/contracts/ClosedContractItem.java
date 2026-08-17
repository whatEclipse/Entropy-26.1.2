package net.entropy.items.contracts;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.Level;

public class ClosedContractItem extends WrittenBookItem {
    public ClosedContractItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        return InteractionResult.SUCCESS;
    }
}