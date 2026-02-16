package net.flansflame.flans_knowledge_lib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public abstract class ServerPacket{

    private static final String PROTOCOL_VERSION = "1";

    protected SimpleChannel simpleChannel;
    protected final String id;

    public int type;

    public ServerPacket(String id) {
        this.id = id;
    }

    public<T extends ServerPacket> void send2Server(int type) {
        T toReturn = (T) this.newSelf();
        toReturn.type = type;
        this.simpleChannel.sendToServer(toReturn);
    }

    public SimpleChannel cast(String modId) {
        this.simpleChannel = NetworkRegistry.newSimpleChannel(new ResourceLocation(modId, this.id),
                () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
        return this.simpleChannel;
    }

    public <T extends ServerPacket> void encode(T packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.type);
    }

    public <T extends ServerPacket> T decode(FriendlyByteBuf buffer) {
        T toReturn = (T) this.newSelf();
        toReturn.type = buffer.readInt();
        return toReturn;
    }

    public <T extends ServerPacket> void handle(T packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            this.run(context.getSender(), packet.type);
        });
        context.setPacketHandled(true);
    }

    protected abstract ServerPacket newSelf();

    protected abstract void run(ServerPlayer player, int type);
}