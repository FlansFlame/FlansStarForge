package net.flansflame.flans_star_forge.entities.ai.end_stellar.custom;

import net.flansflame.flans_star_forge.entities.ai.end_stellar.EndStellarAttackGoal;
import net.flansflame.flans_star_forge.entities.ai.end_stellar.EndStellarAttackPhase;
import net.flansflame.flans_star_forge.entities.entity.StellarEndStageEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class StabEndStellarAttack extends EndStellarAttackPhase {

    public StabEndStellarAttack(String animationId, boolean activateEvenIfNotNear) {
        super(animationId, activateEvenIfNotNear);
    }

    @Override
    public void onAttack(StellarEndStageEntity stellar, LivingEntity target, float amount) {

        if (target == null || stellar.getPerceivedTargetDistanceSquareForMeleeAttack(target) > getAttackReach(stellar, target) * EndStellarAttackGoal.ATTACK_STAGE_RANGE_MULTIPLIER) return;

        super.onAttack(stellar, target, amount);
        target.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 9));
    }
}
