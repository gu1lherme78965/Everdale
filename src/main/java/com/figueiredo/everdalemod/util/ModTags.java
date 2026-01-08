package com.figueiredo.everdalemod.util;

import com.figueiredo.everdalemod.EverdaleMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> ALLOY_INGREDIENT_BLOCK = tag("alloy_ingredient_block");
        public static final TagKey<Block> NEEDS_TIN_TOOL = tag("needs_tin_tool");


        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(EverdaleMod.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> ALLOY_INGREDIENT_ITEM = tag("alloy_ingredient_item");


        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(EverdaleMod.MOD_ID, name));
        }
    }
}
