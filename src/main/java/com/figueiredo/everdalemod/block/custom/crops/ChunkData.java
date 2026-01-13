package com.figueiredo.everdalemod.block.custom.crops;

import com.figueiredo.everdalemod.block.custom.crops.util.Nutrients;
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
    private Nutrients averageNutrients = new Nutrients(0, 0, 0);

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
        if (tag.contains("nutrients")) {
            CompoundTag nutrientsTag = tag.getCompound("nutrients");
            data.averageNutrients = new Nutrients(
                    nutrientsTag.getInt("N"),
                    nutrientsTag.getInt("P"),
                    nutrientsTag.getInt("K")
            );
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

        CompoundTag nutrientsTag = new CompoundTag();
        nutrientsTag.putInt("N", averageNutrients.nitrogen());
        nutrientsTag.putInt("P", averageNutrients.phosphorus());
        nutrientsTag.putInt("K", averageNutrients.potassium());

        tag.put("nutrients", nutrientsTag);

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

    public Nutrients getAverageNutrients() {
        return averageNutrients;
    }

    public void setAverageNutrients(Nutrients averageNutrients) {
        this.averageNutrients = averageNutrients;
    }

    public void smoothNutrients(Nutrients chunkAverageNutrients) {
        int targetNitrogen = chunkAverageNutrients.nitrogen();
        int targetPhosphorus = chunkAverageNutrients.phosphorus();
        int targetPotassium = chunkAverageNutrients.potassium();
        float weigth = 0.75f;

        for (Long2ObjectMap.Entry<SoilContentInformation> entry : entries()) {
            SoilContentInformation info = entry.getValue();
            int currentNitrogen = info.availableNutrients().nitrogen();
            int currentPhosphorus = info.availableNutrients().phosphorus();
            int currentPotassium = info.availableNutrients().potassium();

            entry.setValue( new SoilContentInformation(new Nutrients(
                    currentNitrogen + Math.round((targetNitrogen - currentNitrogen) * weigth),
                    currentPhosphorus + Math.round((targetPhosphorus - currentPhosphorus) * weigth),
                    currentPotassium + Math.round((targetPotassium - currentPotassium) * weigth)
            )).clamp());
        }

        calculateAverage();
    }

    private void calculateAverage() {
        int totalNitrogen = 0;
        int totalPhosphorus = 0;
        int totalPotassium = 0;
        int count = 0;

        for (Long2ObjectMap.Entry<SoilContentInformation> entry : entries()) {
            SoilContentInformation info = entry.getValue();
            totalNitrogen += info.availableNutrients().nitrogen();
            totalPhosphorus += info.availableNutrients().phosphorus();
            totalPotassium += info.availableNutrients().potassium();

            count++;
        }

        if (count == 0) {
            averageNutrients = Nutrients.zero();
            return;
        }

        averageNutrients = new Nutrients(
                totalNitrogen / count,
                totalPhosphorus / count,
                totalPotassium / count
        );
    }

    public void smoothNutrients() {
        smoothNutrients(averageNutrients);
    }
}
