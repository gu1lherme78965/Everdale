package com.figueiredo.everdalemod.block.custom.crops.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.Map;

public class SoilInitializer {

    private static final Map<ResourceKey<Biome>, SoilContentInformation> SOIL_BY_BIOME = Map.ofEntries(
            Map.entry(Biomes.PLAINS,      new SoilContentInformation(new Nutrients(60, 50, 55))),
            Map.entry(Biomes.FOREST,      new SoilContentInformation(new Nutrients(70, 60, 65))),
            Map.entry(Biomes.BIRCH_FOREST,new SoilContentInformation(new Nutrients(65, 55, 60))),
            Map.entry(Biomes.SAVANNA,     new SoilContentInformation(new Nutrients(40, 35, 45))),
            Map.entry(Biomes.DESERT,      new SoilContentInformation(new Nutrients(10, 5, 15))),
            Map.entry(Biomes.SWAMP,       new SoilContentInformation(new Nutrients(80, 70, 75))),
            Map.entry(Biomes.JUNGLE,      new SoilContentInformation(new Nutrients(85, 75, 80))),
            Map.entry(Biomes.TAIGA,       new SoilContentInformation(new Nutrients(50, 40, 45)))
    );

    public static SoilContentInformation create(ServerLevel level, BlockPos pos) {
        ResourceKey<Biome> biomeKey = level.getBiome(pos).unwrapKey().orElse(null);

        if (biomeKey == null) {
            return SoilContentInformation.zero();
        }

        return SOIL_BY_BIOME
                .getOrDefault(biomeKey, SoilContentInformation.zero())
                .clamp();
    }
}
