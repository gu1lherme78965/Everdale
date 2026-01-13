package com.figueiredo.everdalemod.block.custom.crops.util;

public record GrowthResult(
        boolean shouldGrow,
        Nutrients consumedNutrients
) {
}
