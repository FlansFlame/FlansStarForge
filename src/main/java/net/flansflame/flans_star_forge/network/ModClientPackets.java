package net.flansflame.flans_star_forge.network;

import net.flansflame.flans_knowledge_lib.network.ClientPacket;
import net.flansflame.flans_knowledge_lib.network.Server2ClientPacketRegisterer;
import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.network.packet.OpenFakeDeathScreenClientPacket;

public class ModClientPackets {
    public static final Server2ClientPacketRegisterer PACKETS = new Server2ClientPacketRegisterer(FlansStarForge.MOD_ID);

    public static final ClientPacket OPEN_FAKE_DEATH_SCREEN =
            PACKETS.register(new OpenFakeDeathScreenClientPacket("open_fake_death_screen"));

    public static void register() {
        PACKETS.register();
    }
}
