package net.flansflame.flans_star_forge;

import net.flansflame.flans_knowledge_lib.mixin_accesor.IEntityMixinAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class Utils {
    public static void exRemove(Entity entity) {
        entity.level().broadcastEntityEvent(entity, (byte) 60);

        if (entity.getRemovalReason() == null) {
            ((IEntityMixinAccessor) entity).flansKnowledgeLib$setRemovalReason(Entity.RemovalReason.DISCARDED);
        }

        if (entity.getRemovalReason().shouldDestroy()) {
            entity.stopRiding();
        }

        entity.getPassengers().forEach(Entity::stopRiding);
        ((IEntityMixinAccessor) entity).flansKnowledgeLib$getLevelCallback().onRemove(Entity.RemovalReason.DISCARDED);
        entity.invalidateCaps();

        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.getBrain().clearMemories();
        }
    }
}
