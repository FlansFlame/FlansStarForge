package net.flansflame.flans_star_forge.world.ai.end_stellar;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.world.ai.stellar.StellarAttackPhases;
import net.flansflame.flans_star_forge.world.entity.custom.StellarEndStageEntity;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class EndStellarAttackGoal extends MeleeAttackGoal {

    public static final int ATTACK_START_TICK = 60;
    public static final int DELAY_TICK = 100;

    public EndStellarAttackGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity entity, double targetDistance) {
        double d0 = this.getAttackReachSqr(entity);
        if (targetDistance <= d0 && getTicksUntilNextAttack() <= 0 && this.mob instanceof StellarEndStageEntity stellar) {
            this.resetAttackCooldown();

            stellar.setAttackPhase(Mth.nextInt(RandomSource.create(), 0, StellarAttackPhases.ATTACK_PHASES.size() - 1));
            EndStellarAttackPhase attackPhase = EndStellarAttackPhases.ATTACK_PHASES.get(stellar.getAttackPhase());

            attackPhase.beforeAttack(stellar, entity);

            FlansStarForge.queueServerWork(ATTACK_START_TICK, () -> {
                attackPhase.onAttack(stellar, entity);
            });
        }
    }

    @Override
    protected int adjustedTickDelay(int p_186072_) {
        return super.adjustedTickDelay(DELAY_TICK);
    }
}
