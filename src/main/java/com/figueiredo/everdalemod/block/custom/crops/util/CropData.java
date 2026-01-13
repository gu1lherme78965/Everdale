package com.figueiredo.everdalemod.block.custom.crops.util;

public interface CropData {
    String name();
    int maxAge();
    Nutrients nutrientIntake();
    Nutrients nutrientLevelPreferences();
}
