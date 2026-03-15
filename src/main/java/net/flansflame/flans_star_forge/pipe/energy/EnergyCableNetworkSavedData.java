package net.flansflame.flans_star_forge.pipe.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;

public class EnergyCableNetworkSavedData extends SavedData {

    private final List<EnergyCableNetwork> networks = new ArrayList<>();

    public static EnergyCableNetworkSavedData get(ServerLevel server) {
        return server.getDataStorage().computeIfAbsent(
                EnergyCableNetworkSavedData::load,
                EnergyCableNetworkSavedData::new,
                "energy_cable_networks"
        );
    }

    public static EnergyCableNetworkSavedData load(CompoundTag tag) {
        EnergyCableNetworkSavedData data = new EnergyCableNetworkSavedData();
        ListTag list = tag.getList("networksPositions", Tag.TAG_COMPOUND);
        for (Tag pTag : list){
            CompoundTag posTag = (CompoundTag) pTag;
            BlockPos pos = NbtUtils.readBlockPos(posTag);
            EnergyCableNetwork network = new EnergyCableNetwork();

            network.markDirty(pos);
            data.networks.add(network);
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (EnergyCableNetwork network : this.networks){
            for (BlockPos pos : network.pipes){
                list.add(NbtUtils.writeBlockPos(pos));
                break;
            }
        }
        tag.put("networksPositions", list);

        return tag;
    }

    public List<EnergyCableNetwork> getNetworks() {
        return networks;
    }
}
