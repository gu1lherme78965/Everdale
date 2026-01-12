package com.figueiredo.everdalemod.block.custom.crops.util;

import net.minecraft.resources.ResourceLocation;

public record TallCropData(
        String name,
        int maxAge,
        int ageToGrowTop,
        TallCropShapeProfile shapeProfile,
        ResourceLocation seedItem,
        ResourceLocation dropItem
) {
}
