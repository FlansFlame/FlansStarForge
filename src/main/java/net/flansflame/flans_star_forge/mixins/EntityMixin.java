package net.flansflame.flans_star_forge.mixins;

import net.flansflame.flans_knowledge_lib.world.entity.IUnRemovable;
import net.flansflame.flans_star_forge.mixin_accesor.IEntityMixinAccessor;
import net.minecraft.commands.CommandSource;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Entity.class})
public abstract class EntityMixin extends CapabilityProvider<Entity> implements Nameable, EntityAccess, CommandSource, net.minecraftforge.common.extensions.IForgeEntity, IEntityMixinAccessor {

    @Shadow
    private Entity.RemovalReason removalReason;

    @Shadow
    private EntityInLevelCallback levelCallback;

    protected EntityMixin(Class<Entity> baseClass) {
        super(baseClass);
    }

    protected EntityMixin(Class<Entity> baseClass, boolean isLazy) {
        super(baseClass, isLazy);
    }

    @Override
    public void setRemovalReason(Entity.RemovalReason value) {
        this.removalReason = value;
    }

    @Override
    public EntityInLevelCallback getLevelCallback() {
        return levelCallback;
    }

    @Inject(method = "setRemoved", at = @At("HEAD"), cancellable = true)
    public void setRemoved(Entity.RemovalReason p_146876_, CallbackInfo ci) {
        if (this instanceof IUnRemovable) {
            ci.cancel();
        }
    }

    @Inject(method = "remove", at = @At("HEAD"), cancellable = true)
    public void remove(Entity.RemovalReason p_146876_, CallbackInfo ci) {
        if (this instanceof IUnRemovable) {
            ci.cancel();
        }
    }
}
