package com.figueiredo.everdalemod.block.custom.crops.util.simpleCrop;

import com.figueiredo.everdalemod.block.custom.crops.util.CropData;
import com.figueiredo.everdalemod.block.custom.crops.util.Nutrients;
import net.minecraft.resources.ResourceLocation;

public record SimpleCropData(
        String name,
        int maxAge,
        Nutrients nutrientIntake,
        Nutrients nutrientLevelPreferences,
        SimpleCropShapeProfile shapeProfile,
        ResourceLocation seedItem,
        ResourceLocation dropItem
) implements CropData {
}
