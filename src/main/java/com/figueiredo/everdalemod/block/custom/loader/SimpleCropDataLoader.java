package com.figueiredo.everdalemod.block.custom.loader;

import com.figueiredo.everdalemod.EverdaleMod;
import com.figueiredo.everdalemod.block.custom.util.SimpleCropData;
import com.figueiredo.everdalemod.block.custom.util.SimpleCropShapeProfile;
import com.figueiredo.everdalemod.block.custom.util.TallCropData;
import com.figueiredo.everdalemod.block.custom.util.TallCropShapeProfile;
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

            SimpleCropData data = new SimpleCropData(
                    jsonObject.get("name").getAsString(),
                    jsonObject.get("max_age").getAsInt(),
                    SimpleCropShapeProfile.valueOf(jsonObject.get("shape_profile").getAsString().toUpperCase()),
                    new ResourceLocation(jsonObject.get("seed_item").getAsString()),
                    new ResourceLocation(jsonObject.get("drop_item").getAsString())
            );

            CROPS.put(new ResourceLocation(EverdaleMod.MOD_ID, jsonObject.get("name").getAsString()), data);
        });
    }

    public static SimpleCropData get(String name) {
        return CROPS.get(new ResourceLocation(EverdaleMod.MOD_ID, name));
    }
}
