package com.figueiredo.everdalemod;

import com.figueiredo.everdalemod.block.custom.crops.ChunkData;
import com.figueiredo.everdalemod.block.custom.crops.util.Nutrients;
import com.figueiredo.everdalemod.block.custom.crops.util.SoilContentInformation;
import com.figueiredo.everdalemod.block.custom.crops.util.SoilInitializer;
import com.figueiredo.everdalemod.util.ChunkTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid=EverdaleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModForgeEvents {

    private static final List<Block> SOIL_BLOCKS = List.of(
            Blocks.FARMLAND,
            Blocks.DIRT,
            Blocks.GRASS_BLOCK
    );

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        BlockPos pos = event.getPos();
        BlockState state = event.getPlacedBlock();

        if (!isSoil(state.getBlock())) return;

        ChunkPos chunkPos = new ChunkPos(pos);
        ChunkData chunkData = ChunkData.get(level, chunkPos);

        if (chunkData.get(pos).equals(SoilContentInformation.zero())) {
            chunkData.set(pos, SoilInitializer.create(level, pos));
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        BlockPos pos = event.getPos();
        ChunkData.get(level, new ChunkPos(pos)).remove(pos);
    }

    @SubscribeEvent
    public static void onTillGround(BlockEvent.BlockToolModificationEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        // We only care about tilling for now
        if (!event.getToolAction().equals(ToolActions.HOE_TILL)) return;

        BlockPos pos = event.getPos();

        ChunkPos chunkPos = new ChunkPos(pos);
        ChunkData chunkData = ChunkData.get(level, chunkPos);

        if (chunkData.get(pos).equals(SoilContentInformation.zero())) {
            chunkData.set(pos, SoilInitializer.create(level, pos));
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (!(event.level instanceof ServerLevel level)) return;

            // Soil Smoothing, twice per day
            if (level.getGameTime() % 100 != 0) return;
            for (ChunkPos chunkPos : ChunkTracker.loadedChunks) {
                ChunkData chunkData = ChunkData.get(level, chunkPos);
                Nutrients average = chunkData.getAverageNutrients();
                boolean log = !Objects.equals(average, Nutrients.zero());
                if (log) {
                    EverdaleMod.LOGGER.info("Smoothing nutrients at chunk: {}, {}", chunkPos.x, chunkPos.z);
                    EverdaleMod.LOGGER.info("Pre smoothing nutrients: N-{} P-{} K-{}", average.nitrogen(), average.phosphorus(), average.potassium());
                }

                Nutrients chunkAverageNutrients = getNeighbourAverageNutrients(level, chunkPos);
                chunkData.setAverageNutrients(chunkAverageNutrients);
                chunkData.smoothNutrients();
                Nutrients newNutrients = chunkData.getAverageNutrients();

                if (log) {
                    EverdaleMod.LOGGER.info("Post smoothing nutrients: N-{} P-{} K-{}", newNutrients.nitrogen(), newNutrients.phosphorus(), newNutrients.potassium());
                }
            }
        }
    }

    private static Nutrients getNeighbourAverageNutrients(ServerLevel level, ChunkPos chunkPos) {
        int totalNitrogen = 0;
        int totalPhosphorus = 0;
        int totalPotassium = 0;
        int chunkCount = 0;

        for (int x = chunkPos.x - 1; x <= chunkPos.x + 1; ++x) {
            for (int z = chunkPos.z - 1; z <= chunkPos.z + 1; ++z) {
                ChunkData chunkData = ChunkData.get(level, new ChunkPos(x, z));
                totalNitrogen += chunkData.getAverageNutrients().nitrogen();
                totalPhosphorus += chunkData.getAverageNutrients().phosphorus();
                totalPotassium += chunkData.getAverageNutrients().potassium();
                chunkCount++;
            }
        }

        return new Nutrients(
                totalNitrogen / chunkCount,
                totalPhosphorus / chunkCount,
                totalPotassium / chunkCount
        );
    }

    private static boolean isSoil(Block block) {
        return SOIL_BLOCKS.contains(block);
    }
}
