package com.figueiredo.everdalemod.datagen.util;

import com.figueiredo.everdalemod.EverdaleMod;
import com.figueiredo.everdalemod.block.custom.crops.util.SimpleCropData;
import com.figueiredo.everdalemod.block.custom.crops.util.SimpleCropShapeProfile;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SimpleCropRegistry {

    private static boolean initialised = false;
    private static final Map<String, SimpleCropData> DATA = new HashMap<>();
    public static final List<String> CROPS = List.of("strawberry");

    private SimpleCropRegistry() {}

    // called once at datagen initialization
    public static void initialise() {
        if (initialised) return;
        initialised = true;

        // For each JSON file in simple_crops
        for (String cropName : CROPS) {
            SimpleCropData data = loadCropFromResource("data/everdalemod/crops/simple_crops/" + cropName + ".json");
            DATA.put(cropName, data);
        }
    }

    public static SimpleCropData get(String id) {
        SimpleCropData data = DATA.get(id);
        if (data == null) {
            EverdaleMod.LOGGER.warn("Failed to get TallCropData with id {} - using FALLBACK data", id);
            return SimpleCropDefaults.FALLBACK_DATA;
        }
        return data;
    }

    public static Collection<SimpleCropData> getAll() {
        return DATA.values();
    }

    public static Collection<String> getAllResources() {
        return DATA.keySet();
    }

    private static SimpleCropData loadCropFromResource(String path) {
        try (InputStream stream = EverdaleMod.class.getClassLoader().getResourceAsStream(path)) {
            if (stream == null) throw new IllegalStateException("Missing crop file: " + path);

            JsonObject jsonObject = new Gson().fromJson(
                    new InputStreamReader(stream, StandardCharsets.UTF_8),
                    JsonObject.class
            );
            JsonObject cropObject = jsonObject.getAsJsonObject("crop_block");

            String name =  cropObject.get("name").getAsString();
            int maxAge = cropObject.get("max_age").getAsInt();
            SimpleCropShapeProfile shapeProfile = SimpleCropShapeProfile.valueOf(cropObject.get("shape_profile").getAsString().toUpperCase());
            ResourceLocation seedItem = new ResourceLocation(cropObject.get("seed_item_id").getAsString());
            ResourceLocation dropedItem = new ResourceLocation(cropObject.get("drop_item_id").getAsString());

            return new SimpleCropData(
                    name,
                    maxAge,
                    shapeProfile,
                    seedItem,
                    dropedItem
            );
        } catch (IOException e) {
            throw new  IllegalStateException("Failed to load crop file: " + path, e);
        }
    }
}
