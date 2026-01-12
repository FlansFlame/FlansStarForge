package net.flansflame.flans_star_forge.entities.ai.end_stellar;

import net.flansflame.flans_star_forge.entities.ai.stellar.StellarAttackPhases;
import net.flansflame.flans_star_forge.entities.entity.StellarEndStageEntity;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class EndStellarAttackGoal extends MeleeAttackGoal {

    public static final float ATTACK_STAGE_RANGE_MULTIPLIER = 3f;
    public static final int ATTACK_START_TICK = 60;
    public static final int DELAY_TICK = 100;

    public EndStellarAttackGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier, true);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity entity, double targetDistance) {
        double attackRange = this.getAttackReachSqr(entity) * ATTACK_STAGE_RANGE_MULTIPLIER;

        if (this.mob instanceof StellarEndStageEntity stellar && getTicksUntilNextAttack() <= 0) {
            stellar.setAttackPhase(Mth.nextInt(RandomSource.create(), 0, StellarAttackPhases.ATTACK_PHASES.size()));
            EndStellarAttackPhase attackPhase = EndStellarAttackPhases.ATTACK_PHASES.get(stellar.getAttackPhase());

            if (attackPhase.activateEvenIfNotNear()) {
                this.resetAttackCooldown();

                attackPhase.beforeAttack(stellar, entity);

                stellar.setAttackCount(ATTACK_START_TICK);
            } else {
                if (targetDistance <= attackRange) {
                    this.resetAttackCooldown();

                    attackPhase.beforeAttack(stellar, entity);

                    stellar.setAttackCount(ATTACK_START_TICK);
                }
            }
        }
    }

    @Override
    protected int adjustedTickDelay(int p_186072_) {
        return super.adjustedTickDelay(DELAY_TICK);
    }
}
