package net.flansflame.flans_star_forge.world.ai.failed_nova.active;

import net.flansflame.flans_star_forge.world.entity.custom.FailedNovaEntity;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class FailedNovaGoal extends MeleeAttackGoal {

    public static final float ATTACK_STAGE_RANGE_MULTIPLIER = 3f;
    public static final int ATTACK_START_TICK = 60;
    public static final int DELAY_TICK = 100;

    public FailedNovaGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier, true);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity entity, double targetDistance) {
        double attackRange = this.getAttackReachSqr(entity) * ATTACK_STAGE_RANGE_MULTIPLIER;

        if (this.mob instanceof FailedNovaEntity valine && getTicksUntilNextAttack() <= 0) {
            valine.setAttackPhase(Mth.nextInt(RandomSource.create(), 0, FailedNovaActiveSkills.ACTIVE_SKILLS.size() - 1));
            FailedNovaActiveSkill activeSkill = FailedNovaActiveSkills.ACTIVE_SKILLS.get(valine.getAttackPhase());

            if (activeSkill.activateEvenIfNotNear()) {
                this.resetAttackCooldown();

                activeSkill.beforeAttack(valine, entity);

                valine.setAttackCount(ATTACK_START_TICK);
            } else {
                if (targetDistance <= attackRange) {
                    this.resetAttackCooldown();

                    activeSkill.beforeAttack(valine, entity);

                    valine.setAttackCount(ATTACK_START_TICK);
                }
            }
        }
    }

    @Override
    protected int adjustedTickDelay(int delay) {
        return super.adjustedTickDelay(DELAY_TICK);
    }
}
