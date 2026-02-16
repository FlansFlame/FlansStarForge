package net.flansflame.flans_star_forge.entities.ai.failed_nova.actives.active;

import net.flansflame.flans_knowledge_lib.Utils;
import net.flansflame.flans_star_forge.damagesource.ModDamageTypes;
import net.flansflame.flans_star_forge.entities.ai.failed_nova.actives.FailedNovaActiveSkill;
import net.flansflame.flans_star_forge.entities.entity.FailedNovaEntity;
import net.flansflame.flans_star_forge.entities.entity.StellarEndStageEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class ShockWaveActiveSkill extends FailedNovaActiveSkill {

    private static final float WAVE_RADIUS = 12f;

    public ShockWaveActiveSkill(String animationId, SoundEvent attackSound, boolean activateEvenIfNotNear) {
        super(animationId, attackSound, activateEvenIfNotNear);
    }

    @Override
    public void onAttack(FailedNovaEntity nova, LivingEntity target , float amount) {
        double x = nova.getX();
        double y = nova.getY();
        double z = nova.getZ();

        if (nova.level() instanceof ServerLevel server) {
            final Vec3 _center = new Vec3(x, y, z);
            List<LivingEntity> _entfound = server.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center)
                    .inflate(WAVE_RADIUS), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
            for (LivingEntity entity : _entfound) {
                if (entity != nova && (int) entity.getY() == (int) nova.getY()) {
                    if (entity instanceof ServerPlayer serverPlayer && serverPlayer.isSpectator()) continue;

                    entity.hurt(Utils.createDamageSource(server, ModDamageTypes.SONIC_BOOM_WITH_ALL_BYPASS, nova), amount);
                }
            }
            server.sendParticles(ParticleTypes.EXPLOSION_EMITTER, nova.getX(), nova.getY() + 0.5, nova.getZ(), 64, WAVE_RADIUS, 0, WAVE_RADIUS, 0);
        }
    }
}
