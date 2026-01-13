package com.figueiredo.everdalemod.block.custom.crops.loader;

import com.figueiredo.everdalemod.EverdaleMod;
import com.figueiredo.everdalemod.block.custom.crops.util.Nutrients;
import com.figueiredo.everdalemod.block.custom.crops.util.tallCrop.TallCropData;
import com.figueiredo.everdalemod.block.custom.crops.util.tallCrop.TallCropShapeProfile;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class TallCropDataLoader extends SimpleJsonResourceReloadListener {
    public TallCropDataLoader() {
        super(new Gson(), "crops/tall_crops");
    }

    private static final Map<ResourceLocation, TallCropData> CROPS = new HashMap<>();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        CROPS.clear();

        resourceLocationJsonElementMap.forEach((resourceLocation, jsonElement) -> {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            TallCropData data = new TallCropData(
                    jsonObject.get("name").getAsString(),
                    jsonObject.get("max_age").getAsInt(),
                    getNutritionalIntake(jsonObject),
                    getNutritionalPreference(jsonObject),
                    jsonObject.get("age_to_grow_top").getAsInt(),
                    TallCropShapeProfile.valueOf(jsonObject.get("shape_profile").getAsString().toUpperCase()),
                    new ResourceLocation(jsonObject.get("seed_item_id").getAsString()),
                    new ResourceLocation(jsonObject.get("drop_item_id").getAsString())
            );

            CROPS.put(new ResourceLocation(EverdaleMod.MOD_ID, jsonObject.get("name").getAsString()), data);
        });
    }

    private static Nutrients getNutritionalIntake(JsonObject jsonObject) {
        JsonObject nutritionObject = jsonObject.getAsJsonObject("nutrients");
        JsonObject intake = nutritionObject.getAsJsonObject("intake");

        return new Nutrients(
                intake.get("nitrogen").getAsInt(),
                intake.get("phosphorus").getAsInt(),
                intake.get("potassium").getAsInt()
        );
    }

    private static Nutrients getNutritionalPreference(JsonObject jsonObject) {
        JsonObject nutritionObject = jsonObject.getAsJsonObject("nutrients");
        JsonObject intake = nutritionObject.getAsJsonObject("preference");

        return new Nutrients(
                intake.get("nitrogen").getAsInt(),
                intake.get("phosphorus").getAsInt(),
                intake.get("potassium").getAsInt()
        );
    }

    public static TallCropData get(String name) {
        return CROPS.get(new ResourceLocation(EverdaleMod.MOD_ID, name));
    }
}
