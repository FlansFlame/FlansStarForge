package net.flansflame.flans_knowledge_lib.network;

import java.util.ArrayList;

public class Client2ServerPacketRegisterer {

    private static int packetId = 0;

    private final ArrayList<ServerPacket> ServerPACKETS = new ArrayList<>();
    private final String modId;

    public Client2ServerPacketRegisterer(String modId) {
        this.modId = modId;
    }

    public <T extends ServerPacket> T register(T packet) {
        ServerPACKETS.add(packet);
        return packet;
    }

    public void register(){
        for (ServerPacket serverPacket : ServerPACKETS){
            serverPacket.cast(this.modId).registerMessage(packetId++, serverPacket.getClass(), serverPacket::encode, serverPacket::decode, serverPacket::handle);
        }
    }
}
