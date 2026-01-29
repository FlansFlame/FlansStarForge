package net.flansflame.flans_star_forge.pipe.energy;

import net.flansflame.flans_star_forge.blocks.block.EnergyCableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EnergyCableNetworkEvent {

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.level instanceof ServerLevel server)) return;

        EnergyCableNetworkSavedData data = EnergyCableNetworkSavedData.get(server);

        for (EnergyCableNetwork network : data.getNetworks()) {
            network.tick(server);
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof ServerLevel server)) return;

        if (event.getPlacedBlock().hasBlockEntity()) {
            for (Direction direction : Direction.values()) {
                BlockPos neighbor = event.getPos().relative(direction);
                BlockState neighborState = server.getBlockState(neighbor);

                EnergyCableBlock.setOrResetStates(server, neighborState, neighbor);
            }
        }

        handleBlockChange(server, event.getPos());
    }

    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel server)) return;

        handleBlockChange(server, event.getPos());
    }

    private static void handleBlockChange(ServerLevel level, BlockPos pos) {
        EnergyCableNetworkSavedData data = EnergyCableNetworkSavedData.get(level);
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