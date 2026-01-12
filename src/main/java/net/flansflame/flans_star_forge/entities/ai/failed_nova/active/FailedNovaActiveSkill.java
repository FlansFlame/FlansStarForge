package net.flansflame.flans_star_forge.entities.ai.failed_nova.active;

import net.flansflame.flans_knowledge_lib.Utils;
import net.flansflame.flans_star_forge.damagesource.ModDamageTypes;
import net.flansflame.flans_star_forge.entities.entity.FailedNovaEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

public class FailedNovaActiveSkill {
    private final String animationId;
    private final SoundEvent attackSound;
    private final boolean activateEvenIfNotNear;

    public FailedNovaActiveSkill(String animationId, boolean activateEvenIfNotNear) {
        this(animationId, null, activateEvenIfNotNear);
    }

    public FailedNovaActiveSkill(String animationId, SoundEvent attackSound, boolean activateEvenIfNotNear) {
        this.animationId = animationId;
        this.attackSound = attackSound;
        this.activateEvenIfNotNear = activateEvenIfNotNear;
    }

    public final boolean isEmpty() {
        return animationId.isEmpty();
    }

    public final String getAnimationId() {
        return animationId;
    }

    public final SoundEvent getAttackSound() {
        return attackSound;
    }

    public final boolean activateEvenIfNotNear() {
        return activateEvenIfNotNear;
    }

    protected final double getAttackReach(LivingEntity entity, LivingEntity target) {
        return entity.getBbWidth() * 2 * entity.getBbWidth() * 2 + target.getBbWidth();
    }

    /*Overrides*/
    public void onAttack(FailedNovaEntity nova, LivingEntity target, float amount) {

        if (target == null || nova.getPerceivedTargetDistanceSquareForMeleeAttack(target) > getAttackReach(nova, target) * FailedNovaGoal.ATTACK_STAGE_RANGE_MULTIPLIER)
            return;

        nova.swing(InteractionHand.MAIN_HAND);
        if (nova.level() instanceof ServerLevel server) {
            target.hurt(Utils.createDamageSource(server, ModDamageTypes.MOB_ATTACK_WITH_ALL_BYPASS, nova), amount);

            if (this.getAttackSound() != null) {
                server.playSound(null, nova.blockPosition(), this.getAttackSound(), SoundSource.HOSTILE);
            }
        }
    }

    public void beforeAttack(FailedNovaEntity nova, LivingEntity target) {
        nova.trigger(nova, nova.level(), this.getAnimationId());
    }
}
