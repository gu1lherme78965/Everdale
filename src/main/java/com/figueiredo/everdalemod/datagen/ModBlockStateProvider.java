package com.figueiredo.everdalemod.datagen;

import com.figueiredo.everdalemod.EverdaleMod;
import com.figueiredo.everdalemod.block.ModBlocks;
import com.figueiredo.everdalemod.block.custom.StrawberryCropBlock;
import com.figueiredo.everdalemod.block.custom.TallCropBlock;
import com.figueiredo.everdalemod.block.custom.util.TallCropData;
import com.figueiredo.everdalemod.datagen.util.TallCropRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    private static final HashMap<String, ConfiguredModel> TALL_CROP_MODELS = new HashMap<>();

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, EverdaleMod.MOD_ID, exFileHelper);
        TallCropRegistry.initialise();
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.DEEPSLATE_TIN_ORE_BLOCK);
        blockWithItem(ModBlocks.TIN_ORE_BLOCK);

        blockWithItem(ModBlocks.TIN_BLOCK);
        blockWithItem(ModBlocks.RAW_TIN_BLOCK);

        makeSimpleCrop((CropBlock)ModBlocks.STRAWBERRY_CROP.get(), StrawberryCropBlock.AGE, "strawberry_stage", "strawberry_stage");
        makeTallCrop((CropBlock)ModBlocks.CORN_CROP.get(), TallCropBlock.AGE, TallCropBlock.HALF, TallCropRegistry.get("corn"));

    }

    private void makeSimpleCrop(CropBlock cropBlock, IntegerProperty ageProperty, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> simpleCropSates(state, cropBlock, ageProperty, modelName, textureName);

        getVariantBuilder(cropBlock).forAllStates(function);
    }

    private ConfiguredModel[] simpleCropSates(BlockState blockState, CropBlock cropBlock, IntegerProperty ageProperty, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + blockState.getValue(ageProperty),
                    new ResourceLocation(EverdaleMod.MOD_ID, "block/" + textureName + blockState.getValue(ageProperty)))
                    .renderType("cutout"));

        return models;
    }

    private void makeTallCrop(CropBlock cropBlock, IntegerProperty ageProperty, EnumProperty<DoubleBlockHalf> halfProperty, TallCropData data) {
        Function<BlockState, ConfiguredModel[]> function = state -> tallCropStates(state, cropBlock, ageProperty, halfProperty, data);

        getVariantBuilder(cropBlock).forAllStates(function);
    }

    private ConfiguredModel[] tallCropStates(BlockState blockState, CropBlock cropBlock, IntegerProperty ageProperty, EnumProperty<DoubleBlockHalf> halfProperty, TallCropData data) {
        int currentAge = blockState.getValue(ageProperty);
        int maxAge = data.maxAge();

        String suffix = blockState.getValue(halfProperty) == DoubleBlockHalf.LOWER ? "_lower" : "_upper";
        String textureName = data.name() + "_stage_";
        String path = textureName + currentAge + suffix;

        ConfiguredModel[] models = new ConfiguredModel[1];

        if (currentAge > maxAge) {
            models[0] = TALL_CROP_MODELS.get(textureName + maxAge +suffix);
            if (models[0] == null) {
                EverdaleMod.LOGGER.warn("Incapable of generating configured models for {} from cache", path);
            }
            return models;
        };

        models[0] = new ConfiguredModel(models().crop(path,
                    new ResourceLocation(EverdaleMod.MOD_ID, "block/" +  path))
                .renderType("cutout"));

        if (models[0] == null) {
            EverdaleMod.LOGGER.warn("Incapable of generating configured models for {}", path);
        }

        TALL_CROP_MODELS.put(path, models[0]);
        return models;
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
