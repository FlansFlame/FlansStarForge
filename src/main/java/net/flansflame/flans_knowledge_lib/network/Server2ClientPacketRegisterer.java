package net.flansflame.flans_knowledge_lib.network;

import java.util.ArrayList;

public class Server2ClientPacketRegisterer {

    private static int packetId = 0;

    private final ArrayList<ClientPacket> ClientPACKETS = new ArrayList<>();
    private final String modId;

    public Server2ClientPacketRegisterer(String modId) {
        this.modId = modId;
    }

    public <T extends ClientPacket> T register(T packet) {
        ClientPACKETS.add(packet);
        return packet;
    }

    public void register(){
        for (ClientPacket clientPacket : ClientPACKETS){
            clientPacket.cast(this.modId).registerMessage(packetId++, clientPacket.getClass(), clientPacket::encode, clientPacket::decode, clientPacket::handle);
        }
    }
}