package net.flansflame.flans_star_forge.world.entity.custom;

import net.flansflame.flans_knowledge_lib.world.entity.IBossBar;
import net.flansflame.flans_knowledge_lib.world.entity.IUnRemovable;
import net.flansflame.flans_star_forge.mixin_accesor.IEntityMixinAccessor;
import net.flansflame.flans_star_forge.world.ai.end_stellar.EndStellarAttackGoal;
import net.flansflame.flans_star_forge.world.ai.end_stellar.EndStellarAttackPhase;
import net.flansflame.flans_star_forge.world.ai.end_stellar.EndStellarAttackPhases;
import net.flansflame.flans_star_forge.world.entity.ModEntities;
import net.flansflame.flans_star_forge.world.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class StellarEndStageEntity extends Monster implements GeoEntity, RangedAttackMob, IBossBar, IUnRemovable {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final EntityDataAccessor<Integer> ATTACK_PHASE = SynchedEntityData.defineId(StellarEndStageEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> ATTACK_COUNT = SynchedEntityData.defineId(StellarEndStageEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> EX_HP = SynchedEntityData.defineId(StellarEndStageEntity.class, EntityDataSerializers.FLOAT);

    public static final float MAX_EX_HP = 4096f;

    public StellarEndStageEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    /*ATTACKS*/
    @Override
    public void tick() {
        if (this.getAttackCount() >= 0) {
            this.addAttackCount(-1);
        }

        if (this.getAttackCount() == 0) {
            EndStellarAttackPhase attackPhase = EndStellarAttackPhases.ATTACK_PHASES.get(this.getAttackPhase());
            attackPhase.onAttack(this, this.getTarget());

            this.setAttackCount(-1);
        }

        if (!this.isAlive() && this.isDeadOrDying()){
            ++this.deathTime;
            if (this.deathTime >= 20 && !this.isRemoved()) {
                this.level().broadcastEntityEvent(this, (byte)60);
                if (this.getRemovalReason() == null) {
                    ((IEntityMixinAccessor) this).setRemovalReason(RemovalReason.KILLED);
                }

                if (this.getRemovalReason().shouldDestroy()) {
                    this.stopRiding();
                }

                this.getPassengers().forEach(Entity::stopRiding);
                ((IEntityMixinAccessor) this).getLevelCallback().onRemove(RemovalReason.KILLED);
                this.invalidateCaps();
                this.brain.clearMemories();
            }
        }

        super.tick();
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

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));

        for (EndStellarAttackPhase attackPhase : EndStellarAttackPhases.ATTACK_PHASES) {
            create(controllers, attackPhase.getAnimationId());
        }
    }

    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if (tAnimationState.isMoving()) {
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

    public void trigger(StellarEndStageEntity stellarEndStageEntity, Level level, String id) {
        if (level instanceof ServerLevel) stellarEndStageEntity.triggerAnim(id + "_controller", id);
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
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EndStellarAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1.0D, 40, 20.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, false, false, entity ->
                entity.getType() != ModEntities.STELLAR_END_STAGE.get() && entity.getType() != EntityType.WITHER_SKELETON && entity.attackable()
        ));
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder.add(Attributes.MAX_HEALTH, 4);
        builder.add(Attributes.MOVEMENT_SPEED, 0.3f);
        builder.add(Attributes.ATTACK_DAMAGE, 40);
        builder.add(Attributes.ATTACK_SPEED, 1.8);
        return builder;
    }

    private void performRangedAttack(int p_31458_, LivingEntity p_31459_) {
        this.performRangedAttack(p_31458_, p_31459_.getX(), p_31459_.getY() + (double) p_31459_.getEyeHeight() * 0.5D, p_31459_.getZ(), p_31458_ == 0 && this.random.nextFloat() < 0.001F);
    }

    private void performRangedAttack(int i, double pX, double pY, double pZ, boolean b) {
        if (!this.isSilent()) {
            this.level().levelEvent((Player) null, 1024, this.blockPosition(), 0);
        }

        double headX = this.getHeadX(i);
        double headY = this.getHeadY(i) - 1;
        double headZ = this.getHeadZ(i);
        double x = pX - headX;
        double y = pY - headY;
        double z = pZ - headZ;

        WitherSkull witherskull = new WitherSkull(this.level(), this, x, y, z);
        witherskull.setOwner(this);
        if (b) {
            witherskull.setDangerous(true);
        }

        witherskull.setPosRaw(headX, headY, headZ);
        this.level().addFreshEntity(witherskull);
    }

    @Override
    public void performRangedAttack(LivingEntity entity, float v) {
        this.performRangedAttack(0, entity);
    }

    private double getHeadX(int i) {
        if (i <= 0) {
            return this.getX();
        } else {
            float f = (this.yBodyRot + (float) (180 * (i - 1))) * ((float) Math.PI / 180F);
            float f1 = Mth.cos(f);
            return this.getX() + (double) f1 * 1.3D;
        }
    }

    private double getHeadY(int i) {
        return i <= 0 ? this.getY() + 3.0D : this.getY() + 2.2D;
    }

    private double getHeadZ(int i) {
        if (i <= 0) {
            return this.getZ();
        } else {
            float f = (this.yBodyRot + (float) (180 * (i - 1))) * ((float) Math.PI / 180F);
            float f1 = Mth.sin(f);
            return this.getZ() + (double) f1 * 1.3D;
        }
    }

    @Override
    public void checkDespawn() {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.WITHER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
    }

    @Override
    public String getTextureId() {
        return "stellar_end_stage";
    }

    @Override
    public boolean useBarCover() {
        return true;
    }

    @Override
    public float getEntityHpPercentage(LivingEntity entity) {
        if (entity instanceof StellarEndStageEntity stellarEndStage) {
            return stellarEndStage.getExHp() / StellarEndStageEntity.MAX_EX_HP;
        } else {
            return 1f;
        }
    }

    /*SYNCED_DATA*/

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("AttackPhase")) this.setAttackPhase(tag.getInt("AttackPhase")); else this.setAttackPhase(0);
        if (tag.contains("AttackCount")) this.setAttackCount(tag.getInt("AttackCount"));else this.setAttackPhase(-1);
        if (tag.contains("ExHp")) this.setExHp(tag.getFloat("ExHp"));else this.setExHp(MAX_EX_HP);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("AttackPhase", this.getAttackPhase());
        tag.putInt("AttackCount", this.getAttackCount());
        tag.putFloat("ExHp", this.getExHp());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_PHASE, 0);
        this.entityData.define(ATTACK_COUNT, -1);
        this.entityData.define(EX_HP, MAX_EX_HP);
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

    public void setAttackCount(int i) {
        this.entityData.set(ATTACK_COUNT, i);
    }

    public int getAttackCount() {
        return this.entityData.get(ATTACK_COUNT);
    }

    public void addAttackCount(int i) {
        this.setAttackCount(this.getAttackCount() + i);
    }

    /*INVINCIBILITY*/
    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getEntity();
        if (entity instanceof Player player && player.getMainHandItem().is(ModItems.FORGED_STARS_FRAGMENT.get())) {
            this.addExHp(-amount);
            return super.hurt(source, 0f);
        }
        return false;
    }

    @Override
    public float getHealth() {
        return this.getMaxHealth();
    }

    public void setExHp(float exHp) {
        this.entityData.set(EX_HP, exHp);
    }

    public float getExHp() {
        return this.entityData.get(EX_HP);
    }

    public void addExHp(float exHp) {
        this.setExHp(this.getExHp() + exHp);
    }

    @Override
    public void kill() {
        return;
    }

    @Override
    public boolean isAlive() {
        return !this.isRemoved() && this.getExHp() > 0.0F;
    }

    @Override
    public boolean isDeadOrDying() {
        return this.getExHp() <= 0.0F;
    }
}
