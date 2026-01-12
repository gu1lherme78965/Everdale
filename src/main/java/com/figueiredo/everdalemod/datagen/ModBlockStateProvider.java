package com.figueiredo.everdalemod.datagen;

import com.figueiredo.everdalemod.EverdaleMod;
import com.figueiredo.everdalemod.block.ModBlocks;
import com.figueiredo.everdalemod.block.custom.crops.SimpleCropBlock;
import com.figueiredo.everdalemod.block.custom.crops.TallCropBlock;
import com.figueiredo.everdalemod.block.custom.crops.util.simpleCrop.SimpleCropData;
import com.figueiredo.everdalemod.block.custom.crops.util.tallCrop.TallCropData;
import com.figueiredo.everdalemod.datagen.util.SimpleCropRegistry;
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
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    private static final HashMap<String, ConfiguredModel> TALL_CROP_MODELS = new HashMap<>();
    private static final HashMap<String, ConfiguredModel> SIMPLE_CROP_MODELS = new HashMap<>();

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

        registerCropBlocks();
    }

    private void registerCropBlocks() {
        // register TallCropBlocks
        for (String tallCropName : TallCropRegistry.CROPS) {
            makeTallCrop((CropBlock) ForgeRegistries.BLOCKS.getValue(
                    new ResourceLocation(EverdaleMod.MOD_ID, tallCropName.toLowerCase().replace(' ', '_') + "_crop")),
                    TallCropRegistry.get(tallCropName));
        }

        // register SimpleCropBlocks
        for (String simpleCropName : SimpleCropRegistry.CROPS) {
            makeSimpleCrop((CropBlock) ForgeRegistries.BLOCKS.getValue(
                    new ResourceLocation(EverdaleMod.MOD_ID, simpleCropName.toLowerCase().replace(' ', '_') + "_crop")),
                    SimpleCropRegistry.get(simpleCropName));
        }
    }

    private void makeSimpleCrop(CropBlock cropBlock, SimpleCropData data) {
        Function<BlockState, ConfiguredModel[]> function = state -> simpleCropSates(state, cropBlock, SimpleCropBlock.AGE, data);

        getVariantBuilder(cropBlock).forAllStates(function);
    }

    private ConfiguredModel[] simpleCropSates(BlockState blockState, CropBlock cropBlock, IntegerProperty ageProperty, SimpleCropData data) {
        int currentAge = blockState.getValue(ageProperty);
        int maxAge = data.maxAge();

        String TextureName = data.name() + "_stage_";
        String path = TextureName + currentAge;

        ConfiguredModel[] models = new ConfiguredModel[1];

        if (currentAge > maxAge) {
            models[0] = SIMPLE_CROP_MODELS.get(TextureName + maxAge);
            return models;
        }
        models[0] = new ConfiguredModel(models().crop(path,
                    new ResourceLocation(EverdaleMod.MOD_ID, "block/crops/simple/" + data.name().toLowerCase().replace(' ', '/') + '/' + path))
                    .renderType("cutout"));

        if (models[0] == null) {
            EverdaleMod.LOGGER.warn("Incapable of generating configured models for {}", path);
        }

        SIMPLE_CROP_MODELS.put(path, models[0]);
        return models;
    }

    private void makeTallCrop(CropBlock cropBlock, TallCropData data) {
        Function<BlockState, ConfiguredModel[]> function = state -> tallCropStates(state, cropBlock, TallCropBlock.AGE, TallCropBlock.HALF, data);

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
            return models;
        };

        models[0] = new ConfiguredModel(models().crop(path,
                    new ResourceLocation(EverdaleMod.MOD_ID, "block/crops/tall/"+ data.name().toLowerCase().replace(' ', '_') + '/' +  path))
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
