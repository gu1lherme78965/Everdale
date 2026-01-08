package com.figueiredo.everdalemod.datagen;

import com.figueiredo.everdalemod.EverdaleMod;
import com.figueiredo.everdalemod.block.ModBlocks;
import com.figueiredo.everdalemod.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, EverdaleMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ModTags.Blocks.ALLOY_INGREDIENT_BLOCK)
                .add(ModBlocks.TIN_BLOCK.get())
                .add(ModBlocks.RAW_TIN_BLOCK.get())
                .add(ModBlocks.DEEPSLATE_TIN_ORE_BLOCK.get())
                .add(ModBlocks.TIN_ORE_BLOCK.get());

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.TIN_BLOCK.get())
                .add(ModBlocks.RAW_TIN_BLOCK.get())
                .add(ModBlocks.DEEPSLATE_TIN_ORE_BLOCK.get())
                .add(ModBlocks.TIN_ORE_BLOCK.get());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.TIN_BLOCK.get())
                .add(ModBlocks.RAW_TIN_BLOCK.get())
                .add(ModBlocks.DEEPSLATE_TIN_ORE_BLOCK.get())
                .add(ModBlocks.TIN_ORE_BLOCK.get());
    }
}
