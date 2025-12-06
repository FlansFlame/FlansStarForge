package net.flansflame.flans_star_forge.world.entity.custom;

import net.flansflame.flans_star_forge.event.MobStrengthenEvents;
import net.flansflame.flans_star_forge.mixin_accesor.IEntityMixinAccessor;
import net.flansflame.flans_star_forge.world.entity.IOnRemoved;
import net.flansflame.flans_star_forge.world.entity.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Comparator;
import java.util.List;

public class StarsTearEntity extends Mob implements GeoEntity, IOnRemoved {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final int DEFAULT_EXPLODE_TICK = 400;
    public static final int EXPLODE_RADIUS = 128;
    public static final int EXPLODE_DAMAGE = Integer.MAX_VALUE;

    public static final EntityDataAccessor<Integer> EXPLODE_TICK = SynchedEntityData.defineId(StarsTearEntity.class, EntityDataSerializers.INT);

    private boolean removed;

    public StarsTearEntity(EntityType<? extends Mob> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        if (this.getExplodeTick() <= 0) {
            this.explode();
            this.exRemove();
        } else {
            if (this.getExplodeTick() == 50) {
                this.trigger(this, this.level(), "shrink");
            }
            this.addExplodeTick(-1);
        }

        super.tick();
    }

    private void explode() {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        if (this.level() instanceof ServerLevel server) {
            final Vec3 _center = new Vec3(x, y, z);
            List<LivingEntity> _entfound = server.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center)
                    .inflate(EXPLODE_RADIUS), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
            for (LivingEntity entity : _entfound) {
                if (entity != this) {

                    boolean destroyArmor = false;

                    for (ItemStack armor : entity.getArmorSlots()) {
                        if (armor.isEmpty()) continue;

                        if (armor.isDamageableItem()) {
                            armor.setDamageValue(armor.getMaxDamage());
                        }
                        armor.setCount(0);

                        destroyArmor = true;
                    }

                    entity.removeAllEffects();

                    if (destroyArmor && entity instanceof Player player) {
                        player.playSound(SoundEvents.ITEM_BREAK);
                    }

                    entity.hurt(new DamageSource(server.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.SONIC_BOOM)), EXPLODE_DAMAGE * MobStrengthenEvents.BLESSING_ATTACK_MULTIPLIER);
                    server.sendParticles(ParticleTypes.EXPLOSION_EMITTER, entity.getX(), entity.getY() + 0.5, entity.getZ(), 8, 1, 1, 1, 0);
                }
            }
            server.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y + 0.5, x, 8, 0, 0, 0, 0);
            server.playSound(null, this.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE);
        }
    }

    /*GECKOLIB*/
    public void create(AnimatableManager.ControllerRegistrar controllerRegistrar, String id) {
        controllerRegistrar.add(new AnimationController<>(this, id + "_controller", state -> PlayState.STOP)
                .triggerableAnim(id, RawAnimation.begin().then(id, Animation.LoopType.PLAY_ONCE)));
    }

    public void create(AnimatableManager.ControllerRegistrar controllerRegistrar, String id, Animation.LoopType loopType) {
        controllerRegistrar.add(new AnimationController<>(this, id + "_controller", state -> PlayState.STOP)
                .triggerableAnim(id, RawAnimation.begin().then(id, loopType)));
    }

    public void trigger(StarsTearEntity starsTearEntity, Level level, String id) {
        if (level instanceof ServerLevel) starsTearEntity.triggerAnim(id + "_controller", id);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));

        create(controllers, "shrink");
    }

    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        tAnimationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    /*SETTINGS*/
    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    @Override
    public void checkDespawn() {
        return;
    }

    @Override
    public boolean canCollideWith(Entity p_20303_) {
        return false;
    }

    @Override
    protected boolean isImmobile() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder.add(Attributes.MAX_HEALTH, 4);
        return builder;
    }


    /*SYNCED_DATA*/
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("ExplodeTick")) this.setExplodeTick(tag.getInt("ExplodeTick"));

        super.readAdditionalSaveData(tag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("ExplodeTick", this.getExplodeTick());

        super.addAdditionalSaveData(tag);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(EXPLODE_TICK, DEFAULT_EXPLODE_TICK);

        super.defineSynchedData();
    }

    public void setExplodeTick(int tick) {
        this.entityData.set(EXPLODE_TICK, tick);
    }

    public int getExplodeTick() {
        return this.entityData.get(EXPLODE_TICK);
    }

    public void addExplodeTick(int tick) {
        this.setExplodeTick(this.getExplodeTick() + tick);
    }


    /*INVINCIBILITY*/
    @Override
    public float getHealth() {
        return this.getMaxHealth();
    }

    @Override
    public void kill() {
        return;
    }

    @Override
    public void remove(RemovalReason reason) {
        return;
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        return false;
    }

    @Override
    public void onRemove() {
        if (!removed) {
            if (this.level() instanceof ServerLevel server) {
                StarsTearEntity entityToSpawn = ModEntities.STARS_TEAR.get().spawn(server, this.blockPosition(), MobSpawnType.COMMAND);
                if (entityToSpawn != null) {
                    entityToSpawn.setUUID(this.getUUID());
                    entityToSpawn.setYRot(this.getYRot());
                    entityToSpawn.setYHeadRot(this.getYHeadRot());
                    entityToSpawn.setPos(new Vec3(this.getX(), this.getY(), this.getZ()));
                    entityToSpawn.setExplodeTick(this.getExplodeTick());
                }
            }
            removed = true;
        }
    }

    private void exRemove() {
        if (this.getRemovalReason() == null) {
            ((IEntityMixinAccessor) this).setRemovalReason(RemovalReason.DISCARDED);
        }

        if (this.getRemovalReason().shouldDestroy()) {
            this.stopRiding();
        }

        this.getPassengers().forEach(Entity::stopRiding);
        ((IEntityMixinAccessor) this).getLevelCallback().onRemove(RemovalReason.DISCARDED);
        this.invalidateCaps();
        this.brain.clearMemories();
    }
}