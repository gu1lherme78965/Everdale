package com.figueiredo.everdalemod.block.custom.crops.util.simpleCrop;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public final class SimpleCropShapes {

    private static final Map<SimpleCropShapeProfile, VoxelShape[]> SHAPES = Map.of(
            SimpleCropShapeProfile.STRAWBERRY, new VoxelShape[]{
                    box(0, 0, 0, 16, 3, 16),
                    box(0, 0, 0, 16, 6, 16),
                    box(0, 0, 0, 16, 12, 16),
                    box(0, 0, 0, 16, 16, 16),
                    box(0, 0, 0, 16, 16, 16),
                    box(0, 0, 0, 16, 16, 16),
            }
    );

    private static VoxelShape box(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(x1, y1, z1, x2, y2, z2);
    }

    public static VoxelShape[] get(SimpleCropShapeProfile pProfile) {
        return SHAPES.get(pProfile);
    }
}
