package net.flansflame.flans_star_forge.network;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.world.entity.custom.StellarEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModKeyBindMessage {
    int type, pressedms;

    public ModKeyBindMessage(int type, int pressedms) {
        this.type = type;
        this.pressedms = pressedms;
    }

    public ModKeyBindMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
        this.pressedms = buffer.readInt();
    }

    public static void buffer(ModKeyBindMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
        buffer.writeInt(message.pressedms);
    }

    public static void handler(ModKeyBindMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            pressAction(context.getSender(), message.type, message.pressedms);
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, int type, int pressedms) {
        Level level = player.level();
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        if (!level.hasChunkAt(player.blockPosition()))
            return;
        switch (type) {
            case 0 -> {
                if (level instanceof ServerLevel server) {

                    boolean foundStellar = false;

                    for (Entity entity : server.getAllEntities()) {
                        if (entity instanceof StellarEntity stellar && stellar.isOwnedBy(player)) {
                            stellar.setPos(player.getX(), player.getY(), player.getZ());
                            foundStellar = true;
                            break;
                        }
                    }

                    if (!foundStellar && !player.level().isClientSide) {
                        player.displayClientMessage(Component.translatable("entity.flans_star_forge.stellar.failed_to_teleport"), false);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        FlansStarForge.addNetworkMessage(ModKeyBindMessage.class, ModKeyBindMessage::buffer, ModKeyBindMessage::new, ModKeyBindMessage::handler);
    }
}