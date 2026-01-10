package com.figueiredo.everdalemod.block.custom.loader;

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

public class TallCropDataLoader extends SimpleJsonResourceReloadListener {
    public TallCropDataLoader() {
        super(new Gson(), "tall_crops");
    }

    private static final Map<ResourceLocation, TallCropData> CROPS = new HashMap<>();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        CROPS.clear();

        resourceLocationJsonElementMap.forEach((resourceLocation, jsonElement) -> {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            System.out.println(TallCropShapeProfile.valueOf(jsonObject.get("shape_profile").getAsString().toUpperCase()));

            TallCropData data = new TallCropData(
                    jsonObject.get("name").getAsString(),
                    jsonObject.get("max_age").getAsInt(),
                    jsonObject.get("age_to_grow_top").getAsInt(),
                    TallCropShapeProfile.valueOf(jsonObject.get("shape_profile").getAsString().toUpperCase()),
                    new ResourceLocation(jsonObject.get("seed_item").getAsString()),
                    new ResourceLocation(jsonObject.get("drop_item").getAsString())
            );

            CROPS.put(resourceLocation, data);
        });
    }

    public static TallCropData get(ResourceLocation resourceLocation) {
        return CROPS.get(resourceLocation);
    }
}
