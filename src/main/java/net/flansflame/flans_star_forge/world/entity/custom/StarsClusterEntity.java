package net.flansflame.flans_star_forge.world.entity.custom;

import net.flansflame.flans_star_forge.world.ai.StarsClusterGoal;
import net.flansflame.flans_star_forge.world.particle.ModParticles;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class StarsClusterEntity extends TamableAnimal implements GeoEntity {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final EntityDataAccessor<Integer> DEATH_TIME = SynchedEntityData.defineId(StarsClusterEntity.class, EntityDataSerializers.INT);

    public static final int DEATH_TICK = 600;

    public StarsClusterEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }


    /*GECKOLIB*/
    public void create(AnimatableManager.ControllerRegistrar controller, String id) {
        controller.add(new AnimationController<>(this, id + "_controller", state -> PlayState.STOP)
                .triggerableAnim(id, RawAnimation.begin().then(id, Animation.LoopType.PLAY_ONCE)));
    }

    public void create(AnimatableManager.ControllerRegistrar controller, String id, Animation.LoopType loopType) {
        controller.add(new AnimationController<>(this, id + "_controller", state -> PlayState.STOP)
                .triggerableAnim(id, RawAnimation.begin().then(id, loopType)));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        tAnimationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void trigger(StarsClusterEntity stellarEntity, Level level, String id) {
        if (level instanceof ServerLevel) stellarEntity.triggerAnim(id + "_controller", id);
    }


    /*SETTINGS*/
    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.refreshDimensions();
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose).scale((float) 1);
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 12f));
        this.goalSelector.addGoal(2, new FloatGoal(this));
        this.goalSelector.addGoal(3, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(4, new StarsClusterGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.5, 5, 1, false));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder.add(Attributes.MAX_HEALTH, 4);
        builder.add(Attributes.MOVEMENT_SPEED, 0.6f);
        builder.add(Attributes.ATTACK_DAMAGE, 100);
        builder.add(Attributes.ATTACK_SPEED, 1.8f);
        return builder;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (!source.is(DamageTypes.GENERIC_KILL)) {
            return false;
        }
        return super.hurt(source, damage);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel server, AgeableMob mob) {
        return null;
    }


    /*SYNCED_DATA*/
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setDeathTime(tag.getInt("deathTime"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("deathTime", getDeathTime());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DEATH_TIME, DEATH_TICK);
    }

    public int getDeathTime() {
        return this.entityData.get(DEATH_TIME);
    }

    public void setDeathTime(int sitting) {
        this.entityData.set(DEATH_TIME, sitting);
    }

    public void countDownDeathTime() {
        this.setDeathTime(this.getDeathTime() - 1);
    }


    /*TIMER*/
    @Override
    public void tick() {
        if (this.level() instanceof ServerLevel server) {
            server.sendParticles(ModParticles.STARS_CLUSTER.get(), this.getX(), this.getY() + 1.5f, this.getZ(), 1, 0, 0, 0, 0);
        }

        if (this.getDeathTime() <= 0) {
            this.discard();
        } else {
            this.countDownDeathTime();
        }
        super.tick();
    }
}