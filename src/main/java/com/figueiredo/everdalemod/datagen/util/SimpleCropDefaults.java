package com.figueiredo.everdalemod.datagen.util;

import com.figueiredo.everdalemod.block.custom.crops.util.simpleCrop.SimpleCropData;
import com.figueiredo.everdalemod.block.custom.crops.util.simpleCrop.SimpleCropShapeProfile;
import net.minecraft.resources.ResourceLocation;

public final class SimpleCropDefaults {

    public static final SimpleCropData FALLBACK_DATA = new SimpleCropData(
            "wheat",
            7,
            SimpleCropShapeProfile.STRAWBERRY,
            new ResourceLocation("minecraft:wheat_seeds"),
            new ResourceLocation("minecraft:wheat")
    );

    private SimpleCropDefaults() {}
}
