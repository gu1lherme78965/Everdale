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

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public final class DatagenTallCropDataLoader {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<ResourceLocation, TallCropData> DATA = new HashMap<>();

    private DatagenTallCropDataLoader() {}

    public static void loadExistingCrops(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        DATA.clear();

        // Root: data/everdalemod/tall_crops
        Path root = packOutput.getOutputFolder()
                .resolve("data")
                .resolve("everdalemod")
                .resolve("tall_crops");

        if (!Files.exists(root)) return;

        try (Stream<Path> files = Files.walk(root)) {
            files.filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> loadFile(root, path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load tall_crops gen data", e);
        }
    }

    private static void loadFile(Path root, Path file) {
        try (Reader reader = Files.newBufferedReader(file)) {
            JsonObject jsonObject = GSON.fromJson(reader, JsonObject.class);

            TallCropData data = new TallCropData(
                    jsonObject.get("name").getAsString(),
                    jsonObject.get("max_age").getAsInt(),
                    jsonObject.get("age_to_grow_top").getAsInt(),
                    TallCropShapeProfile.valueOf(jsonObject.get("shape_profile").getAsString().toUpperCase()),
                    new ResourceLocation(jsonObject.get("seed_item").getAsString()),
                    new ResourceLocation(jsonObject.get("drop_item").getAsString())
            );

            DATA.put(new ResourceLocation(EverdaleMod.MOD_ID, data.name()), data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load tall_crops data", e);
        }
    }

    public static TallCropData get(ResourceLocation id) {
        return DATA.get(id);
    }

    public static Collection<TallCropData> getAll() {
        return DATA.values();
    }

    public static Collection<ResourceLocation> getAllResources() {
        return DATA.keySet();
    }
}
