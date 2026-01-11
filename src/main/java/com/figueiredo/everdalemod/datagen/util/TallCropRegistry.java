package com.figueiredo.everdalemod.datagen.util;

import com.figueiredo.everdalemod.EverdaleMod;
import com.figueiredo.everdalemod.block.custom.util.TallCropData;
import com.figueiredo.everdalemod.block.custom.util.TallCropShapeProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class TallCropRegistry {

    private static boolean initialised = false;
    private static final Map<String, TallCropData> DATA = new HashMap<>();
    public static final List<String> CROPS = List.of("corn");

    private TallCropRegistry() {}

    // called once at datagen initialization
    public static void initialise() {
        if (initialised) return;
        initialised = true;

        // For each JSON file in tall_crops
        for (String cropName : CROPS) {
            TallCropData data = loadCropFromResource("data/everdalemod/crops/tall_crops/" + cropName + ".json");
            DATA.put(cropName, data);
        }
    }

    public static TallCropData get(String id) {
        TallCropData data = DATA.get(id);
        if (data == null) {
            EverdaleMod.LOGGER.warn("Failed to get TallCropData with id {} - using FALLBACK data", id);
            return TallCropDefaults.FALLBACK_DATA;
        }
        return data;
    }

    public static Collection<TallCropData> getAll() {
        return DATA.values();
    }

    public static Collection<String> getAllResources() {
        return DATA.keySet();
    }

    private static TallCropData loadCropFromResource(String path) {
        try (InputStream stream = EverdaleMod.class.getClassLoader().getResourceAsStream(path)) {
            if (stream == null) throw new IllegalStateException("Missing crop file: " + path);

            JsonObject jsonObject = new Gson().fromJson(
                    new InputStreamReader(stream, StandardCharsets.UTF_8),
                    JsonObject.class
            );
            JsonObject cropObject = jsonObject.getAsJsonObject("crop_block");

            String name =  cropObject.get("name").getAsString();
            int maxAge = cropObject.get("max_age").getAsInt();
            int ageToGrowTop = cropObject.get("age_to_grow_top").getAsInt();
            TallCropShapeProfile shapeProfile = TallCropShapeProfile.valueOf(cropObject.get("shape_profile").getAsString().toUpperCase());
            ResourceLocation seedItem = new ResourceLocation(cropObject.get("seed_item_id").getAsString());
            ResourceLocation dropedItem = new ResourceLocation(cropObject.get("drop_item_id").getAsString());

            return new TallCropData(
                    name,
                    maxAge,
                    ageToGrowTop,
                    shapeProfile,
                    seedItem,
                    dropedItem
            );
        } catch (IOException e) {
            throw new  IllegalStateException("Failed to load crop file: " + path, e);
        }
    }
}
