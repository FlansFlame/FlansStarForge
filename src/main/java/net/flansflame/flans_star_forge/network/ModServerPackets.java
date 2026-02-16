package net.flansflame.flans_star_forge.network;

import net.flansflame.flans_knowledge_lib.network.Client2ServerPacketRegisterer;
import net.flansflame.flans_knowledge_lib.network.ServerPacket;
import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.network.packet.FakeDeathScreenServerPacket;

public class ModServerPackets {
    public static final Client2ServerPacketRegisterer PACKETS = new Client2ServerPacketRegisterer(FlansStarForge.MOD_ID);

    public static final ServerPacket FAKE_DEATH_SCREEN = PACKETS.register(new FakeDeathScreenServerPacket("fake_death_screen"));

    public static void register() {
        PACKETS.register();
    }
}
