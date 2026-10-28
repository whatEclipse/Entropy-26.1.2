package net.entropy.foods;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;

public class EntropyFoods {
    public static final FoodProperties BUTTER = new FoodProperties.Builder().nutrition(2).saturationModifier(0.75F).alwaysEdible().build();
    public static final FoodProperties BUTTERED_BREAD = new FoodProperties.Builder().nutrition(7).saturationModifier(1.1F).build();

    public static final Consumable BUTTER_CONSUMABLE = Consumables.defaultFood().consumeSeconds(1.6f).sound(SoundEvents.GENERIC_EAT).hasConsumeParticles(true).build();
    public static final Consumable BUTTERED_BREAD_CONSUMABLE = Consumables.defaultFood().consumeSeconds(1.6f).sound(SoundEvents.GENERIC_EAT).hasConsumeParticles(true).build();
}
