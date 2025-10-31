package net.flansflame.flans_star_forge.world.ai.custom;

import net.flansflame.flans_star_forge.world.ai.StellarAttackPhase;
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
        super.onAttack(stellar, target);
        target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 4));
    }
}
