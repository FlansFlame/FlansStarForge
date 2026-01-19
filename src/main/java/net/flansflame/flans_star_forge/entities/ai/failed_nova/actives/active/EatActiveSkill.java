package net.flansflame.flans_star_forge.entities.ai.failed_nova.actives.active;

import net.flansflame.flans_knowledge_lib.Utils;
import net.flansflame.flans_star_forge.damagesource.ModDamageTypes;
import net.flansflame.flans_star_forge.entities.ai.failed_nova.actives.FailedNovaActiveSkill;
import net.flansflame.flans_star_forge.entities.entity.FailedNovaEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EatActiveSkill extends FailedNovaActiveSkill {

    public static final float DAMAGE_MULTIPLIER = 5f;

    public EatActiveSkill(String animationId, SoundEvent attackSound, boolean activateEvenIfNotNear) {
        super(animationId, attackSound, activateEvenIfNotNear);
    }

    @Override
    public void onAttack(FailedNovaEntity nova, LivingEntity target, float amount) {

        if (nova.level() instanceof ServerLevel server) {
            Vec3 look = nova.getLookAngle();
            Vec3 origin = nova.position().add(0, 1.0, 0);
            Vec3 center = origin.add(look.scale(3.0));

            AABB field = new AABB(
                    center.x - 2.0, center.y - 1.5, center.z - 2.0,
                    center.x + 2.0, center.y + 1.5, center.z + 2.0
            );

            List<LivingEntity> entities = server.getEntitiesOfClass(LivingEntity.class, field, entity -> entity != nova && entity.isAlive());
            for (LivingEntity entity : entities) {
                entity.hurt(Utils.createDamageSource(server, ModDamageTypes.MOB_ATTACK_WITH_ALL_BYPASS, nova), amount * DAMAGE_MULTIPLIER);
            }
        }
    }
}
