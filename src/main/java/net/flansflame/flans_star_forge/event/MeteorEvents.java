package net.flansflame.flans_star_forge.event;

import net.flansflame.flans_star_forge.variable.ModVariables;
import net.flansflame.flans_star_forge.world.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MeteorEvents {
    @SubscribeEvent
    public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        Level level = player.level();

        if (!ModVariables.hasLoggedIn(player)) {
            if (level instanceof ServerLevel server) {
                double x = player.getX() + Mth.nextInt(RandomSource.create(), -40, 40);
                double z = player.getZ() + Mth.nextInt(RandomSource.create(), -40, 40);

                server.setBlock(BlockPos.containing(x, 319, z), ModBlocks.METEOR.get().defaultBlockState(), 3);
            }
            ModVariables.hasLoggedIn(player, true);
        }
    }
}
