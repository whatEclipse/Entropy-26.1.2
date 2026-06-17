package net.entropy.items;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class ScytheItem extends Item  {
    public ScytheItem(Item.Properties properties) {
        super(properties
                .sword(ToolMaterial.IRON, 7.0F, -2.9F)
                .durability(1782)
                .rarity(Rarity.EPIC)
                .repairable(ItemTags.IRON_TOOL_MATERIALS)
                .component(DataComponents.TOOL, new Tool(
                List.of(Tool.Rule.minesAndDrops((HolderSet<Block>) BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK).getOrThrow(BlockTags.MINEABLE_WITH_HOE)
                        , 9.0F)), 1.0F, 1, true)
                        )
                .component(DataComponents.WEAPON, new Weapon(
                        1,
                        6.0F
                ))
                .component(DataComponents.ATTACK_RANGE, new AttackRange(1, 3.5F, 1, 3.5F, 1, 1))
            );
    }
}

