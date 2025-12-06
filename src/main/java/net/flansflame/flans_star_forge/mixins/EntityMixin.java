package net.flansflame.flans_star_forge.mixins;

import net.flansflame.flans_star_forge.mixin_accesor.IEntityMixinAccessor;
import net.flansflame.flans_star_forge.world.entity.IOnRemoved;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Entity.class})
public abstract class EntityMixin implements IEntityMixinAccessor {

    @Shadow
    private Entity.RemovalReason removalReason;

    @Shadow
    private EntityInLevelCallback levelCallback;

    @Override
    public void setRemovalReason(Entity.RemovalReason value) {
        this.removalReason = value;
    }

    @Override
    public EntityInLevelCallback getLevelCallback() {
        return levelCallback;
    }

    @Inject(method = "setRemoved", at = @At("HEAD"))
    public void onSetRemoved(Entity.RemovalReason reason, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if (self instanceof IOnRemoved) {
            ((IOnRemoved) self).onRemove();
        }
    }
}
