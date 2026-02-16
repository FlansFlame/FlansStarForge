package net.flansflame.flans_star_forge.entities.ai.failed_nova.actives.active;

import net.flansflame.flans_knowledge_lib.Utils;
import net.flansflame.flans_star_forge.damagesource.ModDamageTypes;
import net.flansflame.flans_star_forge.effects.ModEffects;
import net.flansflame.flans_star_forge.entities.ai.failed_nova.actives.FailedNovaActiveSkill;
import net.flansflame.flans_star_forge.entities.entity.FailedNovaEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GrabActiveSkill extends FailedNovaActiveSkill {
    public GrabActiveSkill(String animationId, SoundEvent attackSound, boolean activateEvenIfNotNear) {
        super(animationId, attackSound, activateEvenIfNotNear);
    }

    @Override
    public void onAttack(FailedNovaEntity nova, LivingEntity target, float amount) {

        if (nova.level() instanceof ServerLevel server) {
            Vec3 look = nova.getLookAngle();
            Vec3 origin = nova.position().add(0f, 1f, 0f);
            Vec3 center = origin.add(look.scale(4f));

            AABB field = new AABB(
                    center.x - 6f, nova.getY() - 1f, center.z - 6f,
                    center.x + 6f, nova.getY() + 1f, center.z + 6f
            );

            List<LivingEntity> entities = server.getEntitiesOfClass(LivingEntity.class, field, entity -> entity != nova && entity.isAlive());
            for (LivingEntity entity : entities) {
                if (entity instanceof ServerPlayer serverPlayer && serverPlayer.isSpectator()) continue;

                entity.addEffect(new MobEffectInstance(ModEffects.FROZEN.get(), 60, 0));
                entity.hurt(Utils.createDamageSource(server, ModDamageTypes.MOB_ATTACK_WITH_ALL_BYPASS, nova), amount);
            }
        }
    }
}
