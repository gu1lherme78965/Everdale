package com.figueiredo.everdalemod.block.custom.crops.util.tallCrop;

import com.figueiredo.everdalemod.block.custom.crops.util.CropData;
import com.figueiredo.everdalemod.block.custom.crops.util.Nutrients;
import net.minecraft.resources.ResourceLocation;

public record TallCropData(
        String name,
        int maxAge,
        Nutrients nutrientIntake,
        Nutrients nutrientLevelPreferences,
        int ageToGrowTop,
        TallCropShapeProfile shapeProfile,
        ResourceLocation seedItem,
        ResourceLocation dropItem
) implements CropData {
}
