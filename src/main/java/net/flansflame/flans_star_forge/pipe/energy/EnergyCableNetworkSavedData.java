package net.flansflame.flans_star_forge.pipe.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;

public class EnergyCableNetworkSavedData extends SavedData {

    private final List<EnergyCableNetwork> networks = new ArrayList<>();

    public static EnergyCableNetworkSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                EnergyCableNetworkSavedData::load,
                EnergyCableNetworkSavedData::new,
                "energy_cable_networks"
        );
    }

    public static EnergyCableNetworkSavedData load(CompoundTag tag) {
        return new EnergyCableNetworkSavedData();
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        return tag;
    }

    public List<EnergyCableNetwork> getNetworks() {
        return networks;
    }
}
