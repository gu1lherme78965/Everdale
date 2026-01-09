package com.figueiredo.everdalemod.datagen;

import com.figueiredo.everdalemod.EverdaleMod;
import com.figueiredo.everdalemod.block.ModBlocks;
import com.figueiredo.everdalemod.block.custom.CornCropBlock;
import com.figueiredo.everdalemod.block.custom.StrawberryCropBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, EverdaleMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.DEEPSLATE_TIN_ORE_BLOCK);
        blockWithItem(ModBlocks.TIN_ORE_BLOCK);

        blockWithItem(ModBlocks.TIN_BLOCK);
        blockWithItem(ModBlocks.RAW_TIN_BLOCK);

        makeStrawberryCrop((CropBlock)ModBlocks.STRAWBERRY_CROP.get(), "strawberry_stage", "strawberry_stage");
        makeCornCrop((CropBlock)ModBlocks.CORN_CROP.get(), "corn_stage_", "corn_stage_");

    }

    private void makeStrawberryCrop(CropBlock cropBlock, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> strawberryStates(state, cropBlock, modelName, textureName);

        getVariantBuilder(cropBlock).forAllStates(function);
    }

    private ConfiguredModel[] strawberryStates(BlockState blockState, CropBlock cropBlock, String modelName, String TextureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + blockState.getValue(((StrawberryCropBlock) cropBlock).getAgeProperty()),
                new ResourceLocation(EverdaleMod.MOD_ID, "block/" +  TextureName + blockState.getValue(((StrawberryCropBlock) cropBlock).getAgeProperty())))
                .renderType("cutout"));

        return models;
    }

    private void makeCornCrop(CropBlock cropBlock, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> cornStates(state, cropBlock, modelName, textureName);

        getVariantBuilder(cropBlock).forAllStates(function);
    }

    private ConfiguredModel[] cornStates(BlockState blockState, CropBlock cropBlock, String modelName, String TextureName) {
        String suffix = blockState.getValue(CornCropBlock.HALF) == DoubleBlockHalf.LOWER ? "_lower" : "_upper";
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + blockState.getValue(CornCropBlock.AGE) + suffix,
                        new ResourceLocation(EverdaleMod.MOD_ID, "block/" +  TextureName + blockState.getValue(CornCropBlock.AGE) + suffix))
                .renderType("cutout"));

        return models;
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
