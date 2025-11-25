package net.flansflame.flans_star_forge.mixin_accesor;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityInLevelCallback;

public interface IEntityMixinAccessor {
    void setRemovalReason(Entity.RemovalReason value);

    EntityInLevelCallback getLevelCallback();
}
