package com.figueiredo.everdalemod.util;

import com.figueiredo.everdalemod.EverdaleMod;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid= EverdaleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChunkTracker {
    public static final Set<ChunkPos> loadedChunks = ConcurrentHashMap.newKeySet();

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        loadedChunks.add(event.getChunk().getPos());
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event) {
        loadedChunks.remove(event.getChunk().getPos());
    }
}
