package com.figueiredo.everdalemod.block.custom.crops.util.tallCrop;

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
