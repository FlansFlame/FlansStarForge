package net.flansflame.flans_knowledge_lib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public abstract class ClientPacket {

    private static final String PROTOCOL_VERSION = "1";

    protected SimpleChannel simpleChannel;
    protected final String id;

    public ClientPacket(String id) {
        this.id = id;
    }

    public void send2Player(ServerPlayer player) {
        this.simpleChannel.send(PacketDistributor.PLAYER.with(() -> player), this.newSelf());
    }

    public SimpleChannel cast(String modId) {
        this.simpleChannel = NetworkRegistry.newSimpleChannel(new ResourceLocation(modId, this.id),
                () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
        return this.simpleChannel;
    }

    public <T extends ClientPacket> void encode(T packet, FriendlyByteBuf buffer) {
    }

    public <T extends ClientPacket> T decode(FriendlyByteBuf buffer) {
        return (T) this.newSelf();
    }

    public <T extends ClientPacket> void handle(T packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(this::run);
        context.setPacketHandled(true);
    }

    protected abstract ClientPacket newSelf();

    protected abstract void run();
}