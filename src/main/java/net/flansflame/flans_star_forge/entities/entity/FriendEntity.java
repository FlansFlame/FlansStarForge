package net.flansflame.flans_star_forge.entities.entity;

import net.flansflame.flans_knowledge_lib.mixin_accesor.IEntityMixinAccessor;
import net.flansflame.flans_knowledge_lib.world.entity.IOnRemoved;
import net.flansflame.flans_star_forge.config.CommonConfig;
import net.flansflame.flans_star_forge.entities.ModEntities;
import net.flansflame.flans_star_forge.entities.ai.friend.FriendMeleeAttackGoal;
import net.flansflame.flans_star_forge.items.ModItems;
import net.flansflame.flans_star_forge.tag.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.UUID;

public class FriendEntity extends Monster implements GeoEntity, IOnRemoved {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final EntityDataAccessor<Float> EX_HP = SynchedEntityData.defineId(FriendEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<String> OWNER_UID = SynchedEntityData.defineId(FriendEntity.class, EntityDataSerializers.STRING);

    public static float MAX_EX_HP = 40f;
    public static float ATTACK_DAMAGE = 8f;

    public FriendEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isAlive() && this.isDeadOrDying()) {
            this.exDeath();
        } else {
            this.unsetRemoved();
        }

        if (this.getExHp() > MAX_EX_HP) {
            this.setExHp(MAX_EX_HP);
        }
    }

    /*GECKOLIB*/
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if (tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

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
        this.goalSelector.addGoal(2, new FriendMeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder.add(Attributes.MAX_HEALTH, 20);
        builder.add(Attributes.MOVEMENT_SPEED, 0.3f);
        builder.add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE);
        builder.add(Attributes.ATTACK_SPEED, 1.8);
        return builder;
    }

    @Override
    public void checkDespawn() {
    }


    /*SYNCED_DATA*/
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("ExHp")) this.setExHp(tag.getFloat("ExHp"));
        else this.setExHp(MAX_EX_HP);
        if (tag.contains("Owner")) this.setOwnerUid(tag.getString("Owner"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("ExHp", this.getExHp());
        tag.putString("Owner", this.getOwnerUid());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(EX_HP, MAX_EX_HP);
        this.entityData.define(OWNER_UID, "");
    }

    public void tame(LivingEntity entity) {
        this.setOwnerUid(entity.getStringUUID());
    }

    public boolean isTamed() {
        return !this.getOwnerUid().isEmpty();
    }

    public boolean isTamedBy(LivingEntity entity) {
        return this.getOwnerUid().equals(entity.getStringUUID());
    }

    public Entity getOwner() {
        if (!this.isTamed()) return null;

        if (this.level() instanceof ServerLevel server) {
            return server.getEntity(UUID.fromString(this.getOwnerUid()));
        }
        return null;
    }

    public void setOwnerUid(String uid) {
        this.entityData.set(OWNER_UID, uid);
    }

    public String getOwnerUid() {
        return this.entityData.get(OWNER_UID);
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

    public void damageExHp(float exHp) {
        float modExHp = exHp;
        if (this.getExHp() - exHp < 0) {
            modExHp += this.getExHp() - exHp;
        }
        this.addExHp(-modExHp);
    }

    public void exHeal(float amount) {
        if (amount <= 0f) return;

        float exHealth = this.getHealth();
        if (exHealth > 0f) {
            this.setHealth(this.getExHp() + amount);
        }
    }


    /*INVINCIBILITY*/
    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getEntity();
        if (entity instanceof Player player && isDamageableWeapon(player.getMainHandItem())) {
            this.addExHp(-amount);
            return super.hurt(player.damageSources().playerAttack(player), 0f);
        }
        return false;
    }

    public static boolean isDamageableWeapon(ItemStack itemStack) {
        if (CommonConfig.LIGHT_MODE.get()) {
            return itemStack.is(ModTags.Items.WEAPON_WITH_BLESSING);
        } else {
            return itemStack.is(ModItems.FORGED_STARS_FRAGMENT.get()) || itemStack.is(ModItems.STARS_FRAGMENT.get());
        }
    }

    @Override
    public float getHealth() {
        return this.getExHp() > 1 ? this.getExHp() : 0.5f;
    }

    @Override
    public void setHealth(float amount) {
        return;
    }

    @Override
    public void heal(float amount) {
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

    @Override
    public void remove(RemovalReason reason) {
        return;
    }

    public void setBrain(Brain<?> brain) {
        this.brain = brain;
    }

    private boolean removed;

    @Override
    public void flansKnowledgeLib$onRemoved() {
        if (!removed) {
            if (this.level() instanceof ServerLevel server) {
                FriendEntity entityToSpawn = ModEntities.FRIEND.get().spawn(server, this.blockPosition(), MobSpawnType.COMMAND);
                if (entityToSpawn != null) {
                    entityToSpawn.setExHp(this.getExHp());
                    entityToSpawn.setUUID(this.getUUID());
                    entityToSpawn.setYRot(this.getYRot());
                    entityToSpawn.setYHeadRot(this.getYHeadRot());
                    entityToSpawn.setPos(new Vec3(this.getX(), this.getY(), this.getZ()));
                    entityToSpawn.setBrain(this.getBrain());
                }
            }
            removed = true;
        }
    }

    @Override
    public void kill() {
        return;
    }

    public void exDeath() {
        ++this.deathTime;
        if (this.deathTime >= 20 && !this.isRemoved()) {

            if (this.level() instanceof ServerLevel server) {
                ModEntities.WITHER_BOMB.get().spawn(server, BlockPos.containing(this.position()), MobSpawnType.COMMAND);
            }

            this.level().broadcastEntityEvent(this, (byte) 60);

            if (this.getRemovalReason() == null) {
                ((IEntityMixinAccessor) this).flansKnowledgeLib$setRemovalReason(RemovalReason.KILLED);
            }

            if (this.getRemovalReason().shouldDestroy()) {
                this.stopRiding();
            }

            this.getPassengers().forEach(Entity::stopRiding);
            ((IEntityMixinAccessor) this).flansKnowledgeLib$getLevelCallback().onRemove(RemovalReason.KILLED);
            this.invalidateCaps();
            this.brain.clearMemories();
        }
    }
}
