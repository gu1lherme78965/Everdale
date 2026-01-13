package com.figueiredo.everdalemod.datagen.util;

import com.figueiredo.everdalemod.block.custom.crops.util.Nutrients;
import com.figueiredo.everdalemod.block.custom.crops.util.simpleCrop.SimpleCropData;
import com.figueiredo.everdalemod.block.custom.crops.util.simpleCrop.SimpleCropShapeProfile;
import net.minecraft.resources.ResourceLocation;

public final class SimpleCropDefaults {

    public static final SimpleCropData FALLBACK_DATA = new SimpleCropData(
            "wheat",
            7,
            new Nutrients(1, 1, 1),
            new Nutrients(50, 50, 50),
            SimpleCropShapeProfile.STRAWBERRY,
            new ResourceLocation("minecraft:wheat_seeds"),
            new ResourceLocation("minecraft:wheat")
    );

    private SimpleCropDefaults() {}
}
