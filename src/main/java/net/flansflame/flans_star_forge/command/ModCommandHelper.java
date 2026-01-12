package net.flansflame.flans_star_forge.command;

import net.flansflame.flans_star_forge.variable.ModVariables;
import net.flansflame.flans_star_forge.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

class ModCommandHelper {
    static void reDropMeteor(Player player) {
        if (player == null) return;

        if (!ModVariables.hasLoggedIn(player) || !ModVariables.starsBlessing(player)) {
            player.sendSystemMessage(Component.translatable("cmd.flans_star_forge.re_drop_meteor.fail"));
            return;
        }

        Level level = player.level();

        if (level instanceof ServerLevel server) {
            double x = player.getX() + Mth.nextInt(RandomSource.create(), -40, 40);
            double z = player.getZ() + Mth.nextInt(RandomSource.create(), -40, 40);

            server.setBlock(BlockPos.containing(x, 319, z), ModBlocks.METEOR.get().defaultBlockState(), 3);
        }
    }
}