package com.figueiredo.everdalemod.item;

import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties STRAWBERRY =
            new FoodProperties.Builder().alwaysEat().fast().nutrition(2).saturationMod(0.2f).build();
}
