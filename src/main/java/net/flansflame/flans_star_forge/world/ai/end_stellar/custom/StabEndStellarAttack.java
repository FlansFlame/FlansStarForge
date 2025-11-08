package net.flansflame.flans_star_forge.world.ai.end_stellar.custom;

import net.flansflame.flans_star_forge.world.ai.end_stellar.EndStellarAttackGoal;
import net.flansflame.flans_star_forge.world.ai.end_stellar.EndStellarAttackPhase;
import net.flansflame.flans_star_forge.world.entity.custom.StellarEndStageEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class StabEndStellarAttack extends EndStellarAttackPhase {
    public StabEndStellarAttack(String animationId) {
        super(animationId);
    }

    @Override
    public void onAttack(StellarEndStageEntity stellar, LivingEntity target) {

        if (target == null || stellar.getPerceivedTargetDistanceSquareForMeleeAttack(target) > getAttackReach(stellar, target) * EndStellarAttackGoal.ATTACK_STAGE_RANGE_MULTIPLIER) return;

        super.onAttack(stellar, target);
        target.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 9));
    }
}
