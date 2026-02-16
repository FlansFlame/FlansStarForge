package net.flansflame.flans_knowledge_lib.network;

public class SimpleClientPacket extends ClientPacket {

    private final Runnable runnable;

    public SimpleClientPacket(String id, Runnable runnable) {
        super(id);
        this.runnable = runnable;
    }

    @Override
    protected ClientPacket newSelf() {
        return new SimpleClientPacket(this.id, this.runnable);
    }

    @Override
    protected void run() {
        this.runnable.run();
    }
}
