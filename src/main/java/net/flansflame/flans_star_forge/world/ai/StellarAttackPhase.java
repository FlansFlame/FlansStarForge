package net.flansflame.flans_star_forge.world.ai;

import net.flansflame.flans_star_forge.world.entity.custom.StellarEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

public class StellarAttackPhase {
    private String animationId;
    private SoundEvent attackSound;

    public StellarAttackPhase(String animationId) {
        this(animationId, null);
    }

    public StellarAttackPhase(String animationId, SoundEvent attackSound) {
        this.animationId = animationId;
        this.attackSound = attackSound;
    }

    public boolean isEmpty() {
        return animationId.isEmpty();
    }

    public String getAnimationId() {
        return animationId;
    }

    public void setAnimationId(String animationId) {
        this.animationId = animationId;
    }

    public SoundEvent getAttackSound() {
        return attackSound;
    }

    public void setAttackSound(SoundEvent attackSound) {
        this.attackSound = attackSound;
    }



    /*Overrides*/
    public void onAttack(StellarEntity stellar, LivingEntity target) {
        stellar.swing(InteractionHand.MAIN_HAND);
        stellar.doHurtTarget(target);

        if (stellar.level() instanceof ServerLevel server && this.getAttackSound() != null) {
            server.playSound(null, stellar.blockPosition(), this.getAttackSound(), SoundSource.HOSTILE);
        }
    }

    public void beforeAttack(StellarEntity stellar, LivingEntity target) {
        stellar.trigger(stellar, stellar.level(), this.getAnimationId());
    }
}