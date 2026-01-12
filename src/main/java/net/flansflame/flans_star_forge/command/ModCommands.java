package net.flansflame.flans_star_forge.command;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ModCommands {

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal(FlansStarForge.MOD_ID).requires(player -> player.hasPermission(4))
                .then(Commands.literal("re_drop_meteor")
                        .then(Commands.argument("player", EntityArgument.player()).executes(arguments -> {
                            Player player = arguments.getSource().getPlayer();
                            ModCommandHelper.reDropMeteor(player);
                            return 0;
                        }))
                )
        );
    }
}