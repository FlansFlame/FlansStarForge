package net.flansflame.flans_star_forge.pipe.energy;

import net.flansflame.flans_star_forge.blocks.block.EnergyCableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EnergyCableBlockWatcher {

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        handleBlockChange(level, event.getPos());
    }

    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        handleBlockChange(level, event.getPos());
    }

    private static void handleBlockChange(ServerLevel level, BlockPos pos) {EnergyCableNetworkSavedData data = EnergyCableNetworkSavedData.get(level);
        for (Direction dir : Direction.values()) {
            BlockPos neighbor = pos.relative(dir);

            if (level.getBlockState(neighbor).getBlock() instanceof EnergyCableBlock) {
                markNetworksDirtyAt(level, neighbor, data);
            }
        }
    }

    private static void markNetworksDirtyAt(ServerLevel level, BlockPos pipePos, EnergyCableNetworkSavedData data) {
        for (EnergyCableNetwork net : data.getNetworks()) {
            if (net.pipes.contains(pipePos)) {
                net.markDirty(pipePos);
            }
        }

        data.setDirty();
    }
}