package net.flansflame.flans_star_forge.world.entity.custom;

import net.flansflame.flans_star_forge.world.ai.end_stellar.EndStellarAttackGoal;
import net.flansflame.flans_star_forge.world.ai.end_stellar.EndStellarAttackPhase;
import net.flansflame.flans_star_forge.world.ai.end_stellar.EndStellarAttackPhases;
import net.flansflame.flans_star_forge.world.ai.stellar.StellarAttackPhase;
import net.flansflame.flans_star_forge.world.ai.stellar.StellarAttackPhases;
import net.flansflame.flans_star_forge.world.entity.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
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

public class StellarEndStageEntity extends Monster implements GeoEntity, RangedAttackMob {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private final ServerBossEvent bossInfo = createBossBar();

    public static final EntityDataAccessor<Integer> ATTACK_PHASE = SynchedEntityData.defineId(StellarEndStageEntity.class, EntityDataSerializers.INT);

    public StellarEndStageEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    /*ATTACKS*/

    /*
    public void attack(double x, double y, double z, Level level) {
        int random = Mth.nextInt(RandomSource.create(), 0, 2);
        final double HEAD_P1 = y + 4;
        if (level instanceof ServerLevel server) {
            switch (random) {
                case 1 -> {
                    server.sendParticles(DustParticleOptions.REDSTONE, x, HEAD_P1, z, 16, 0.1, 0.1, 0.1, 0);
                    FlansStarForge.queueServerWork(4, () -> {
                        this.trigger(this, server, "slash");
                        FlansStarForge.queueServerWork(18, () -> Attacks.darkBurst(this, x, y, z, server));
                    });
                }
                case 2 -> {
                    server.sendParticles(ParticleTypes.END_ROD, x, HEAD_P1, z, 16, 0.1, 0.1, 0.1, 0);
                    FlansStarForge.queueServerWork(4, () -> {
                        this.trigger(this, level, "staff");
                        FlansStarForge.queueServerWork(20, () -> Attacks.summonFollower(this, x, y, z, server));
                    });
                }
                case 3 -> {
                    FlansStarForge.queueServerWork(4, () -> {
                        this.trigger(this, level, "hold");
                    });
                }
                case 4 -> {
                    FlansStarForge.queueServerWork(4, () -> {
                        this.trigger(this, level, "up");
                    });
                }
                case 5 -> {
                    FlansStarForge.queueServerWork(4, () -> {
                        this.trigger(this, level, "yeet");
                    });
                }
                default -> this.addAttackPhase(40);
            }
        }
    }
     */

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
        builder.add(Attributes.MAX_HEALTH, 1024);
        builder.add(Attributes.ARMOR, 64);
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

    /*BOSS_BARS*/

    protected ServerBossEvent createBossBar() {
        return new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_6);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
        this.bossInfo.setName(Component.literal(this.getDisplayName().getString()));
    }

    /*SYNCED_DATA*/

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setAttackPhase(tag.getInt("AttackPhase"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("AttackPhase", getAttackPhase());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_PHASE, 0);
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
}
