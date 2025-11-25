package net.flansflame.flans_star_forge.world.ai.stellar.custom;

import net.flansflame.flans_star_forge.world.ai.end_stellar.EndStellarAttackGoal;
import net.flansflame.flans_star_forge.world.ai.stellar.StellarAttackPhase;
import net.flansflame.flans_star_forge.world.entity.custom.StellarEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class StabStellarAttack extends StellarAttackPhase {
    public StabStellarAttack(String animationId) {
        super(animationId);
    }

    @Override
    public void onAttack(StellarEntity stellar, LivingEntity target) {

        if (target == null || stellar.getPerceivedTargetDistanceSquareForMeleeAttack(target) > getAttackReach(stellar, target) * EndStellarAttackGoal.ATTACK_STAGE_RANGE_MULTIPLIER) return;

        super.onAttack(stellar, target);
        target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 4));
    }
}
