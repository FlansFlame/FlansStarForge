package net.flansflame.flans_star_forge.world.entity.custom;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.componet.ModComponentTags;
import net.flansflame.flans_star_forge.world.ai.stellar.StellarAttackGoal;
import net.flansflame.flans_star_forge.world.ai.stellar.StellarAttackPhase;
import net.flansflame.flans_star_forge.world.ai.stellar.StellarAttackPhases;
import net.flansflame.flans_star_forge.world.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.world.ForgeChunkManager;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class StellarEntity extends TamableAnimal implements GeoEntity{

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(StellarEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> ATTACK_PHASE = SynchedEntityData.defineId(StellarEntity.class, EntityDataSerializers.INT);

    public StellarEntity(EntityType<? extends TamableAnimal> type, Level level) {
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

        for (StellarAttackPhase attackPhase : StellarAttackPhases.ATTACK_PHASES) {
            create(controllers, attackPhase.getAnimationId());
        }
    }

    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if (this.isSitting()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("neel", Animation.LoopType.HOLD_ON_LAST_FRAME));
            return PlayState.CONTINUE;
        } else if (tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.HOLD_ON_LAST_FRAME));
            return PlayState.CONTINUE;
        }

        tAnimationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void trigger(StellarEntity stellarEntity, Level level, String id) {
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
        this.goalSelector.addGoal(4, new StellarAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.5, 5, 1, false));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder.add(Attributes.MAX_HEALTH, 20);
        builder.add(Attributes.MOVEMENT_SPEED, 0.6f);
        builder.add(Attributes.ATTACK_DAMAGE, 12);
        builder.add(Attributes.ATTACK_SPEED, 1.8f);
        return builder;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (!source.is(DamageTypes.GENERIC_KILL) || this.isTame()) {
            return false;
        }
        return super.hurt(source, damage);
    }

    @Override
    public void checkDespawn() {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ALLAY_AMBIENT_WITH_ITEM;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ALLAY_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ALLAY_DEATH;
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
        setAttackPhase(tag.getInt("AttackPhase"));
        setSitting(tag.getBoolean("isSitting"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("AttackPhase", getAttackPhase());
        tag.putBoolean("isSitting", isSitting());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_PHASE, 0);
        this.entityData.define(SITTING, false);
    }

    public void setAttackPhase(int i) {
        this.entityData.set(ATTACK_PHASE, i);
    }

    public void setAttackPhase() {
        this.entityData.set(ATTACK_PHASE, 0);
    }

    public int getAttackPhase() {
        return this.entityData.get(ATTACK_PHASE);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }

    public void triggerSitting() {
        setSitting(!isSitting());
    }



    /*CHUNK_LOADING*/

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (!level().isClientSide) {
            ChunkPos chunkPos = new ChunkPos(this.blockPosition());
            ForgeChunkManager.forceChunk((ServerLevel) level(), FlansStarForge.MOD_ID, this.getUUID(), chunkPos.x, chunkPos.z, true, true);
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        if (!level().isClientSide) {
            ChunkPos chunkPos = new ChunkPos(this.blockPosition());
            ForgeChunkManager.forceChunk((ServerLevel) level(), FlansStarForge.MOD_ID, this.getUUID(), chunkPos.x, chunkPos.z, false, true);
        }
        super.remove(reason);
    }



    /*STARS_POWERSTONE STORING & SITTING*/

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.isOwnedBy(player) && !this.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
            ItemStack itemStack = player.getItemInHand(hand);
            if (itemStack.is(ModItems.STARS_POWERSTONE.get())) {
                if (ModComponentTags.OWNER_UUID.get(itemStack).isEmpty()) {
                    ModComponentTags.OWNER_UUID.set(itemStack, player.getStringUUID());
                    this.discard();
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.PASS;
            } else {
                this.setSitting(!isSitting());
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
