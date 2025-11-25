package net.flansflame.flans_star_forge.world.ai;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class StarsClusterGoal extends MeleeAttackGoal {
    public StarsClusterGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity entity, double targetDistance) {
        if (targetDistance <= this.getAttackReachSqr(entity) && getTicksUntilNextAttack() <= 0) {
            this.resetAttackCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(entity);
            if (this.mob.level() instanceof ServerLevel server) {
                server.sendParticles(ParticleTypes.EXPLOSION, entity.getX(), entity.getY(), entity.getZ(), 2, 0, 0, 0, 0);
                server.sendParticles(ParticleTypes.EXPLOSION, this.mob.getX(), this.mob.getY(), this.mob.getZ(), 2, 0, 0, 0, 0);
            }
            this.mob.discard();
        }
    }
}