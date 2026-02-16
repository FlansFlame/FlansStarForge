package net.flansflame.flans_star_forge.network.packet;

import net.flansflame.flans_knowledge_lib.network.ClientPacket;
import net.flansflame.flans_star_forge.screens.screen.FakeDeathScreen;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class OpenFakeDeathScreenClientPacket extends ClientPacket {

    protected Vec3 position;

    public OpenFakeDeathScreenClientPacket(String id) {
        super(id);
    }

    public void send2Player(ServerPlayer player, Vec3 position) {
        OpenFakeDeathScreenClientPacket toReturn = (OpenFakeDeathScreenClientPacket) newSelf();
        toReturn.position = position;
        this.simpleChannel.send(PacketDistributor.PLAYER.with(() -> player), toReturn);
    }

    @Override
    public <T extends ClientPacket> void encode(T packet, FriendlyByteBuf buffer) {
        super.encode(packet, buffer);
        buffer.writeVector3f(((OpenFakeDeathScreenClientPacket) packet).position.toVector3f());
    }

    @Override
    public <T extends ClientPacket> T decode(FriendlyByteBuf buffer) {
        OpenFakeDeathScreenClientPacket toReturn = super.decode(buffer);
        toReturn.position = new Vec3(buffer.readVector3f());
        return (T) toReturn;
    }

    @Override
    public <T extends ClientPacket> void handle(T packet, Supplier<NetworkEvent.Context> contextSupplier) {
        this.position = ((OpenFakeDeathScreenClientPacket) packet).position;
        super.handle(packet, contextSupplier);
    }

    @Override
    protected ClientPacket newSelf() {
        return new OpenFakeDeathScreenClientPacket(this.id);
    }

    @Override
    protected void run() {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null) return;

        Minecraft.getInstance().options.setCameraType(CameraType.FIRST_PERSON);
        Minecraft.getInstance().setScreen(new FakeDeathScreen(Component.translatable("deathScreen.title"), this.position));
    }
}