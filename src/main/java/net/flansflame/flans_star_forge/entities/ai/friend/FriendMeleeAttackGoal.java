package net.flansflame.flans_star_forge.entities.ai.friend;

import net.flansflame.flans_knowledge_lib.Utils;
import net.flansflame.flans_star_forge.damagesource.ModDamageTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class FriendMeleeAttackGoal extends MeleeAttackGoal {
    public FriendMeleeAttackGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity entity, double targetDistance) {
        double range = this.getAttackReachSqr(entity);
        if (targetDistance <= range && this.getTicksUntilNextAttack() <= 0) {
            this.resetAttackCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);

            if (entity.level() instanceof ServerLevel server) {
                float attackDamage = (float) this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE);
                entity.hurt(Utils.createDamageSource(server, ModDamageTypes.MOB_ATTACK_WITH_ALL_BYPASS, this.mob), attackDamage);
            }
        }
    }
}
