package net.entropy.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.AttackRange;

public class SharkToothCleaverItem extends Item {
    public SharkToothCleaverItem(Properties properties) {
        super(properties
                .rarity(Rarity.UNCOMMON)
                .sword(ToolMaterial.IRON, 1.0F, -1.6F)
                .durability(1980)
                .repairable(ItemTags.IRON_TOOL_MATERIALS)
                .component(DataComponents.ATTACK_RANGE, new AttackRange(0, 2.5F, 0, 2.5F, 1, 1))
        );
    }
}
