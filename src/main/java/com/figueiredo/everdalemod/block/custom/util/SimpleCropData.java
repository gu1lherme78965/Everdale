package com.figueiredo.everdalemod.block.custom.util;

import net.minecraft.resources.ResourceLocation;

public record SimpleCropData(
        String name,
        int maxAge,
        SimpleCropShapeProfile shapeProfile,
        ResourceLocation seedItem,
        ResourceLocation dropItem
) {
}
