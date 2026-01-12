package com.figueiredo.everdalemod;

import com.figueiredo.everdalemod.block.custom.crops.ChunkData;
import com.figueiredo.everdalemod.block.custom.crops.util.SoilContentInformation;
import com.figueiredo.everdalemod.block.custom.crops.util.SoilInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid=EverdaleMod.MOD_ID)
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

        if (!isSoil(state)) return;

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

    private static boolean isSoil(BlockState state) {
        return SOIL_BLOCKS.contains(state.getBlock());
    }
}
