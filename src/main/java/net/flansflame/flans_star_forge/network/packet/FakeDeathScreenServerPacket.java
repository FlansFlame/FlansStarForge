package net.flansflame.flans_star_forge.network.packet;

import net.flansflame.flans_knowledge_lib.mixin_accesor.IEntityMixinAccessor;
import net.flansflame.flans_knowledge_lib.network.ServerPacket;
import net.flansflame.flans_star_forge.entities.ModEntities;
import net.flansflame.flans_star_forge.entities.entity.FailedNovaEntity;
import net.flansflame.flans_star_forge.entities.entity.StellarEndStageEntity;
import net.flansflame.flans_star_forge.entities.entity.StellarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class FakeDeathScreenServerPacket extends ServerPacket {

    protected Vec3 position;

    public FakeDeathScreenServerPacket(String id) {
        super(id);
    }

    public void send2Server(int type, Vec3 position) {
        FakeDeathScreenServerPacket toReturn = (FakeDeathScreenServerPacket) this.newSelf();
        toReturn.type = type;
        toReturn.position = position;
        this.simpleChannel.sendToServer(toReturn);
    }

    @Override
    public <T extends ServerPacket> void encode(T packet, FriendlyByteBuf buffer) {
        super.encode(packet, buffer);
        buffer.writeVector3f(((FakeDeathScreenServerPacket) packet).position.toVector3f());
    }

    @Override
    public <T extends ServerPacket> T decode(FriendlyByteBuf buffer) {
        FakeDeathScreenServerPacket toReturn = super.decode(buffer);
        toReturn.position = new Vec3(buffer.readVector3f());
        return (T) toReturn;
    }

    @Override
    public <T extends ServerPacket> void handle(T packet, Supplier<NetworkEvent.Context> contextSupplier) {
        this.position = ((FakeDeathScreenServerPacket) packet).position;
        super.handle(packet, contextSupplier);
    }

    @Override
    protected ServerPacket newSelf() {
        return new FakeDeathScreenServerPacket(this.id);
    }

    @Override
    protected void run(ServerPlayer player, int type) {
        ServerLevel server = (ServerLevel) player.level();

        if (type == 0) {
            final Vec3 center = player.position();
            List<StellarEntity> stellarEntities = server.getEntitiesOfClass(StellarEntity.class, new AABB(center, center).inflate(FailedNovaEntity.PASSIVE_SKILL_RADIUS), e -> true);

            for (StellarEntity stellar : stellarEntities) {
                if (stellar.isOwnedBy(player)) {
                    deleteLivingEntity(server, stellar);
                    StellarEndStageEntity endStellar = ModEntities.STELLAR_END_STAGE.get().spawn(server, BlockPos.containing(this.position), MobSpawnType.COMMAND);
                    if (endStellar != null){
                        endStellar.tame(player);
                    }
                }
            }

            player.setPos(this.position);
            player.setGameMode(GameType.SURVIVAL);
        }
    }

    private static void deleteLivingEntity(ServerLevel server, LivingEntity entity) {
        server.sendParticles(ParticleTypes.EXPLOSION, entity.getX(), entity.getY(), entity.getZ(), 2, 0f, 0f, 0f, 0f);

        if (entity.getRemovalReason() == null) {
            ((IEntityMixinAccessor) entity).flansKnowledgeLib$setRemovalReason(Entity.RemovalReason.DISCARDED);
        }

        if (entity.getRemovalReason().shouldDestroy()) {
            entity.stopRiding();
        }

        entity.getPassengers().forEach(Entity::stopRiding);
        ((IEntityMixinAccessor) entity).flansKnowledgeLib$getLevelCallback().onRemove(Entity.RemovalReason.DISCARDED);
        entity.invalidateCaps();
        entity.getBrain().clearMemories();
    }
}
