package com.figueiredo.everdalemod.item;

import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties STRAWBERRY =
            new FoodProperties.Builder().alwaysEat().fast().nutrition(2).saturationMod(0.2f).build();
    public static final FoodProperties CORN =
            new FoodProperties.Builder().alwaysEat().nutrition(3).saturationMod(0.3f).build();
}
