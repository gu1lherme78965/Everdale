package com.figueiredo.everdalemod.block.custom.crops.loader;

import com.figueiredo.everdalemod.EverdaleMod;
import com.figueiredo.everdalemod.block.custom.crops.util.SimpleCropData;
import com.figueiredo.everdalemod.block.custom.crops.util.SimpleCropShapeProfile;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class SimpleCropDataLoader extends SimpleJsonResourceReloadListener {
    public SimpleCropDataLoader() {
        super(new Gson(), "crops/simple_crops");
    }

    private static final Map<ResourceLocation, SimpleCropData> CROPS = new HashMap<>();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        CROPS.clear();

        resourceLocationJsonElementMap.forEach((resourceLocation, jsonElement) -> {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonObject cropObject = jsonObject.getAsJsonObject("crop_block");

            SimpleCropData data = new SimpleCropData(
                    cropObject.get("name").getAsString(),
                    cropObject.get("max_age").getAsInt(),
                    SimpleCropShapeProfile.valueOf(cropObject.get("shape_profile").getAsString().toUpperCase()),
                    new ResourceLocation(cropObject.get("seed_item_id").getAsString()),
                    new ResourceLocation(cropObject.get("drop_item_id").getAsString())
            );

            CROPS.put(new ResourceLocation(EverdaleMod.MOD_ID, cropObject.get("name").getAsString()), data);
        });
    }

    public static SimpleCropData get(String name) {
        return CROPS.get(new ResourceLocation(EverdaleMod.MOD_ID, name));
    }
}
