package net.flansflame.flans_star_forge.world.ai.end_stellar;

import net.flansflame.flans_star_forge.world.entity.custom.StellarEndStageEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

public class EndStellarAttackPhase {
    private final String animationId;
    private final SoundEvent attackSound;
    private final boolean activateEvenIfNotNear;

    public EndStellarAttackPhase(String animationId, boolean activateEvenIfNotNear) {
        this(animationId, null, activateEvenIfNotNear);
    }

    public EndStellarAttackPhase(String animationId, SoundEvent attackSound, boolean activateEvenIfNotNear) {
        this.animationId = animationId;
        this.attackSound = attackSound;
        this.activateEvenIfNotNear = activateEvenIfNotNear;
    }

    public boolean isEmpty() {
        return animationId.isEmpty();
    }

    public String getAnimationId() {
        return animationId;
    }

    public SoundEvent getAttackSound() {
        return attackSound;
    }

    public boolean activateEvenIfNotNear() {
        return activateEvenIfNotNear;
    }

    protected double getAttackReach(LivingEntity entity, LivingEntity target) {
        return entity.getBbWidth() * 2 * entity.getBbWidth() * 2 + target.getBbWidth();
    }

    /*Overrides*/
    public void onAttack(StellarEndStageEntity stellar, LivingEntity target) {

        if (target == null || stellar.getPerceivedTargetDistanceSquareForMeleeAttack(target) > getAttackReach(stellar, target) * EndStellarAttackGoal.ATTACK_STAGE_RANGE_MULTIPLIER)
            return;

        stellar.swing(InteractionHand.MAIN_HAND);
        stellar.doHurtTarget(target);

        if (stellar.level() instanceof ServerLevel server && this.getAttackSound() != null) {
            server.playSound(null, stellar.blockPosition(), this.getAttackSound(), SoundSource.HOSTILE);
        }
    }

    public void beforeAttack(StellarEndStageEntity stellar, LivingEntity target) {
        stellar.trigger(stellar, stellar.level(), this.getAnimationId());
    }
}