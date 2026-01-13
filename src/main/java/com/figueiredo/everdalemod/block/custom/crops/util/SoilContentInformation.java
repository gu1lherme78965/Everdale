package com.figueiredo.everdalemod.block.custom.crops.util;

import net.minecraft.nbt.CompoundTag;

public record SoilContentInformation(
        Nutrients availableNutrients
) {
    public static SoilContentInformation zero() {
        return new SoilContentInformation(new Nutrients(0,0,0));
    }

    public SoilContentInformation clamp() {
        return new SoilContentInformation( new Nutrients(
                Math.min(100, Math.max(0, this.availableNutrients().nitrogen())),
                Math.min(100, Math.max(0, this.availableNutrients().phosphorus())),
                Math.min(100, Math.max(0, this.availableNutrients().potassium()))
        ));
    }

    // ---------------- NBT ----------------

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("N", this.availableNutrients().nitrogen());
        tag.putInt("P", this.availableNutrients().phosphorus());
        tag.putInt("K", this.availableNutrients().potassium());
        return tag;
    }

    public static SoilContentInformation load(CompoundTag tag) {
        return new SoilContentInformation(new Nutrients(
                tag.getInt("N"),
                tag.getInt("P"),
                tag.getInt("K")
        )).clamp();
    }
}
