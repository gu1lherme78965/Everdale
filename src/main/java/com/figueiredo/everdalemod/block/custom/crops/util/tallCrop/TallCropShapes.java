package com.figueiredo.everdalemod.block.custom.crops.util.tallCrop;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public final class TallCropShapes {

    public static final Map<TallCropShapeProfile, VoxelShape[]> LOWER = Map.of(
            TallCropShapeProfile.CORN, new VoxelShape[]{
                    box(0, 0, 0, 16, 2, 16),
                    box(0, 0, 0, 16, 4, 16),
                    box(0, 0, 0, 16, 6, 16),
                    box(0, 0, 0, 16, 8, 16),
                    box(0, 0, 0, 16, 10, 16),
                    box(0, 0, 0, 16, 12, 16),
                    box(0, 0, 0, 16, 14, 16),
                    box(0, 0, 0, 16, 16, 16),
                    box(0, 0, 0, 16, 16, 16),
            }
    );

    public static final Map<TallCropShapeProfile, VoxelShape[]> UPPER = Map.of(
            TallCropShapeProfile.CORN, new VoxelShape[]{
                    box(0, 0, 0, 16, 14, 16),
            }
    );

    private static VoxelShape box(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(x1, y1, z1, x2, y2, z2);
    }
}
