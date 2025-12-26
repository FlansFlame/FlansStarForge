package net.flansflame.flans_star_forge.mixins;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.emotion.EmotionStats;
import net.flansflame.flans_star_forge.mixin_accesor.IPlayerMixinAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Player.class})
public abstract class PlayerMixin implements IPlayerMixinAccessor {
    @Unique
    private static final EntityDataAccessor<Integer> flansStarForge$SANITY = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> flansStarForge$FATIGUE = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> flansStarForge$MOTIVATION = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    public void readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains(FlansStarForge.MOD_ID + "-" + "sanity") && tag.contains(FlansStarForge.MOD_ID + "-" + "fatigue") && tag.contains(FlansStarForge.MOD_ID + "-" + "motivation")) {
            this.flansStarForge$setStats(new int[]{
                    tag.getInt(FlansStarForge.MOD_ID + "-" + "sanity"),
                    tag.getInt(FlansStarForge.MOD_ID + "-" + "fatigue"),
                    tag.getInt(FlansStarForge.MOD_ID + "-" + "motivation")
            });
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    public void addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        tag.putInt(FlansStarForge.MOD_ID + "-" + "sanity", this.flansStarForge$getSanity());
        tag.putInt(FlansStarForge.MOD_ID + "-" + "fatigue", this.flansStarForge$getFatigue());
        tag.putInt(FlansStarForge.MOD_ID + "-" + "motivation", this.flansStarForge$getMotivation());
    }

    @Inject(method = "defineSynchedData", at = @At("HEAD"))
    public void defineSynchedData(CallbackInfo ci) {
        Player flansStarForge$self = (Player) (Object) this;

        flansStarForge$self.getEntityData().define(flansStarForge$SANITY, EmotionStats.DEFAULT_STATS);
        flansStarForge$self.getEntityData().define(flansStarForge$FATIGUE, EmotionStats.DEFAULT_STATS);
        flansStarForge$self.getEntityData().define(flansStarForge$MOTIVATION, EmotionStats.DEFAULT_STATS);
    }

    @Override
    public int flansStarForge$getSanity() {
        Player flansStarForge$self = (Player) (Object) this;
        return EmotionStats.makeValid(flansStarForge$self.getEntityData().get(flansStarForge$SANITY));
    }

    @Override
    public int flansStarForge$getFatigue() {
        Player flansStarForge$self = (Player) (Object) this;

        return EmotionStats.makeValid(flansStarForge$self.getEntityData().get(flansStarForge$FATIGUE));
    }

    @Override
    public int flansStarForge$getMotivation() {
        Player flansStarForge$self = (Player) (Object) this;

        return EmotionStats.makeValid(flansStarForge$self.getEntityData().get(flansStarForge$MOTIVATION));
    }

    @Override
    public void flansStarForge$setSanity(int sanity) {
        Player flansStarForge$self = (Player) (Object) this;

        flansStarForge$self.getEntityData().set(flansStarForge$SANITY, EmotionStats.makeValid(sanity));
    }

    @Override
    public void flansStarForge$setFatigue(int fatigue) {
        Player flansStarForge$self = (Player) (Object) this;

        flansStarForge$self.getEntityData().set(flansStarForge$FATIGUE, EmotionStats.makeValid(fatigue));
    }

    @Override
    public void flansStarForge$setMotivation(int motivation) {
        Player flansStarForge$self = (Player) (Object) this;

        flansStarForge$self.getEntityData().set(flansStarForge$MOTIVATION, EmotionStats.makeValid(motivation));
    }
}