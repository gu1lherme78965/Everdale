package com.figueiredo.everdalemod.block.custom.crops.util;

import net.minecraft.nbt.CompoundTag;

public record SoilContentInformation(
        int nitrogen,
        int phosphorus,
        int potassium
) {
    public static SoilContentInformation zero() {
        return new SoilContentInformation(0,0,0);
    }

    public SoilContentInformation clamp() {
        return new SoilContentInformation(
                Math.min(100, Math.max(0, this.nitrogen)),
                Math.min(100, Math.max(0, this.phosphorus)),
                Math.min(100, Math.max(0, this.potassium))
        );
    }

    // ---------------- NBT ----------------

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("N", nitrogen);
        tag.putInt("P", phosphorus);
        tag.putInt("K", potassium);
        return tag;
    }

    public static SoilContentInformation load(CompoundTag tag) {
        return new SoilContentInformation(
                tag.getInt("N"),
                tag.getInt("P"),
                tag.getInt("K")
        ).clamp();
    }
}
