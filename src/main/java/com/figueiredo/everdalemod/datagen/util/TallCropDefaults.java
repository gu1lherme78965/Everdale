package com.figueiredo.everdalemod.datagen.util;

import com.figueiredo.everdalemod.block.custom.crops.util.Nutrients;
import com.figueiredo.everdalemod.block.custom.crops.util.tallCrop.TallCropData;
import com.figueiredo.everdalemod.block.custom.crops.util.tallCrop.TallCropShapeProfile;
import net.minecraft.resources.ResourceLocation;

public final class TallCropDefaults {

    public static final TallCropData FALLBACK_DATA = new TallCropData(
            "wheat",
            7,
            new Nutrients(1, 1, 1),
            new Nutrients(50, 50, 50),
            4,
            TallCropShapeProfile.CORN,
            new ResourceLocation("minecraft:wheat_seeds"),
            new ResourceLocation("minecraft:wheat")
    );

    private TallCropDefaults() {}
}
