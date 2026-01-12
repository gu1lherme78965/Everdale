package com.figueiredo.everdalemod.block.custom.crops;

import com.figueiredo.everdalemod.block.custom.crops.util.SoilContentInformation;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Collection;

public class ChunkData extends SavedData {

    private final Long2ObjectMap<SoilContentInformation> map = new Long2ObjectOpenHashMap<>();

    public static ChunkData get(ServerLevel level, ChunkPos pos) {
        return level.getDataStorage().computeIfAbsent(
                ChunkData::load,
                ChunkData::new,
                "soil_" + pos.x + "_" + pos.z
        );
    }

    private static ChunkData load(CompoundTag tag) {
        ChunkData data = new ChunkData();

        if (tag.contains("map")) {
            ListTag list = tag.getList("map", CompoundTag.TAG_COMPOUND);

            for (int i=0; i<list.size(); ++i) {
                CompoundTag entry = list.getCompound(i);

                long pos = entry.getLong("pos");
                SoilContentInformation info = SoilContentInformation.load(entry.getCompound("info"));

                data.map.put(pos, info);
            }
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();

        for (Long2ObjectMap.Entry<SoilContentInformation> entry : map.long2ObjectEntrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putLong("pos", entry.getLongKey());
            entryTag.put("info", entry.getValue().save());
            list.add(entryTag);
        }

        tag.put("map", list);
        return tag;
    }

    public SoilContentInformation get(BlockPos pos) {
        SoilContentInformation info = map.get(pos.asLong());
        return info == null ? SoilContentInformation.zero() : info;
    }

    public void set (BlockPos pos, SoilContentInformation soil) {
        map.put(pos.asLong(), soil);
        setDirty();
    }

    public void remove (BlockPos pos) {
        if (map.get(pos.asLong()) == null) return;

        map.remove(pos.asLong());
        setDirty();
    }

    public Collection<Long2ObjectMap.Entry<SoilContentInformation>> entries() {
        return map.long2ObjectEntrySet();
    }
}
