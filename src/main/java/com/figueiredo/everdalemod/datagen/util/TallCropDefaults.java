package com.figueiredo.everdalemod.datagen.util;

import com.figueiredo.everdalemod.block.custom.crops.util.TallCropData;
import com.figueiredo.everdalemod.block.custom.crops.util.TallCropShapeProfile;
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
