package com.figueiredo.everdalemod.block.custom.crops;

import com.figueiredo.everdalemod.block.custom.crops.util.GrowthContext;
import com.figueiredo.everdalemod.block.custom.crops.util.GrowthResult;
import com.figueiredo.everdalemod.block.custom.crops.util.Nutrients;

public class GrowthSolver {
    public static GrowthResult calculateGrowth(GrowthContext growthContext) {
        Nutrients preference = growthContext.cropData().nutrientLevelPreferences();
        Nutrients intake = growthContext.cropData().nutrientIntake();

        Nutrients available = growthContext.soilContentInformation().availableNutrients();

        float nutrientModifier = getNutrientModifier(available, preference);

        boolean shouldGrow = 2 * nutrientModifier > 1;
        Nutrients consumption = calculateConsumption(available, intake);

        return new GrowthResult(shouldGrow, consumption);
    }

    private static Nutrients calculateConsumption(Nutrients available, Nutrients intake) {
        return new Nutrients(
                Math.min(available.nitrogen(), intake.nitrogen()),
                Math.min(available.phosphorus(), intake.phosphorus()),
                Math.min(available.potassium(), intake.potassium())
        );
    }

    private static float getNutrientModifier(Nutrients available, Nutrients preference) {
        // calculates the average between all modifiers
        float nitrogenModifier = (float) available.nitrogen() / preference.nitrogen();
        float phosphorusModifier = (float) available.phosphorus() / preference.phosphorus();
        float potassiumModifier = (float) available.potassium() / preference.potassium();

        return (nitrogenModifier + phosphorusModifier +  potassiumModifier) / 3;
    }

    public static Nutrients getLeftoverNutrients(Nutrients available, Nutrients intake) {
        return new Nutrients(
                available.nitrogen() - intake.nitrogen(),
                available.phosphorus() - intake.phosphorus(),
                available.potassium() - intake.potassium()
        );
    }
}
