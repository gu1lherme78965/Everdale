package com.figueiredo.everdalemod.block.custom.crops.util;

public record Nutrients(
        int nitrogen,
        int phosphorus,
        int potassium
) {
    public static Nutrients zero() {
        return new Nutrients(0, 0, 0);
    }
}
