package com.figueiredo.everdalemod.block.custom.util;

public record SoilInformation(
        int nitrogen,
        int phosphorus,
        int potassium
) {
    public static SoilInformation zero() {
        return new SoilInformation(0,0,0);
    }

    public SoilInformation clamp() {
        return new SoilInformation(
                Math.min(100, Math.max(0, this.nitrogen)),
                Math.min(100, Math.max(0, this.phosphorus)),
                Math.min(100, Math.max(0, this.potassium))
        );
    }
}
