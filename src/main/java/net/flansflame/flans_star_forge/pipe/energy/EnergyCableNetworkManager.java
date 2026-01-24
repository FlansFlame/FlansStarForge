package net.flansflame.flans_star_forge.pipe.energy;

import net.flansflame.flans_star_forge.blocks.block.EnergyCableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

public class EnergyCableNetworkManager {

    public static void pipePlaced(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel server)) return;

        EnergyCableNetworkSavedData data = EnergyCableNetworkSavedData.get(server);
        Set<EnergyCableNetwork> touching = findTouchingNetworks(data, pos);

        if (touching.isEmpty()) {
            EnergyCableNetwork net = new EnergyCableNetwork();
            net.markDirty(pos);
            data.getNetworks().add(net);
        } else if (touching.size() == 1) {
            touching.iterator().next().markDirty(pos);
        } else {
            EnergyCableNetwork merged = new EnergyCableNetwork();
            merged.markDirty(pos);
            data.getNetworks().removeAll(touching);
            data.getNetworks().add(merged);
        }

        data.setDirty();
    }

    public static void pipeRemoved(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel server)) return;

        EnergyCableNetworkSavedData data = EnergyCableNetworkSavedData.get(server);

        for (Direction dir : Direction.values()) {
            BlockPos next = pos.relative(dir);
            if (server.getBlockState(next).getBlock() instanceof EnergyCableBlock) {
                EnergyCableNetwork net = new EnergyCableNetwork();
                net.markDirty(next);
                data.getNetworks().add(net);
            }
        }

        data.setDirty();
    }

    private static Set<EnergyCableNetwork> findTouchingNetworks(EnergyCableNetworkSavedData data, BlockPos pos) {
        Set<EnergyCableNetwork> result = new HashSet<>();

        for (EnergyCableNetwork net : data.getNetworks()) {
            for (Direction dir : Direction.values()) {
                if (net.pipes.contains(pos.relative(dir))) {
                    result.add(net);
                }
            }
        }
        return result;
    }

    public static void markDirtyAt(Level level, BlockPos pipePos) {
        if (!(level instanceof ServerLevel server)) return;

        EnergyCableNetworkSavedData data = EnergyCableNetworkSavedData.get(server);

        for (EnergyCableNetwork net : data.getNetworks()) {
            if (net.pipes.contains(pipePos)) {
                net.markDirty(pipePos);
            }
        }

        data.setDirty();
    }

}
