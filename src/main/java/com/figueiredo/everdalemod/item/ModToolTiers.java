package com.figueiredo.everdalemod.item;

import com.figueiredo.everdalemod.EverdaleMod;
import com.figueiredo.everdalemod.util.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class ModToolTiers {
    public static final Tier TIN = TierSortingRegistry.registerTier(
            new ForgeTier(1, 150, 4.5f, 1.5f, 7,
                    ModTags.Blocks.NEEDS_TIN_TOOL, () -> Ingredient.of(ModItems.TIN.get())),
            new ResourceLocation(EverdaleMod.MOD_ID, "tin"), List.of(Tiers.STONE), List.of(Tiers.IRON));
}
