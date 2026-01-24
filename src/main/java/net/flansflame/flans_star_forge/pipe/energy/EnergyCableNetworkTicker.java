package net.flansflame.flans_star_forge.pipe.energy;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EnergyCableNetworkTicker {

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.level instanceof ServerLevel server)) return;

        EnergyCableNetworkSavedData data = EnergyCableNetworkSavedData.get(server);

        for (EnergyCableNetwork network : data.getNetworks()) {
            network.tick(server);
        }
    }
}