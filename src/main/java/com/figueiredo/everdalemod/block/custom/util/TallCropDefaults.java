package com.figueiredo.everdalemod.block.custom.util;

import net.minecraft.resources.ResourceLocation;

public final class TallCropDefaults {

    public static final TallCropData FALLBACK_DATA = new TallCropData(
            "wheat",
            7,
            4,
            TallCropShapeProfile.CORN,
            new ResourceLocation("minecraft:wheat_seeds"),
            new ResourceLocation("minecraft:wheat")
    );

    private TallCropDefaults() {}
}
