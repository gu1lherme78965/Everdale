package com.figueiredo.everdalemod.datagen;

import com.figueiredo.everdalemod.EverdaleMod;
import com.figueiredo.everdalemod.item.ModItems;
import com.figueiredo.everdalemod.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, EverdaleMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ModTags.Items.ALLOY_INGREDIENT_ITEM)
                .add(ModItems.RAW_TIN.get())
                .add(ModItems.TIN.get());

        this.tag(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.TIN_HELMET.get())
                .add(ModItems.TIN_CHESTPLATE.get())
                .add(ModItems.TIN_LEGGINGS.get())
                .add(ModItems.TIN_BOOTS.get());
    }
}
