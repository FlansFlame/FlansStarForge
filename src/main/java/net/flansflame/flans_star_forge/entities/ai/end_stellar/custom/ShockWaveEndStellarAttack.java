package net.flansflame.flans_star_forge.entities.ai.end_stellar.custom;

import net.flansflame.flans_knowledge_lib.Utils;
import net.flansflame.flans_star_forge.entities.ai.end_stellar.EndStellarAttackPhase;
import net.flansflame.flans_star_forge.damagesource.ModDamageTypes;
import net.flansflame.flans_star_forge.entities.entity.StellarEndStageEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class ShockWaveEndStellarAttack extends EndStellarAttackPhase {

    private static final int WAVE_RADIUS = 8;

    public ShockWaveEndStellarAttack(String animationId, SoundEvent attackSound, boolean activateEvenIfNotNear) {
        super(animationId, attackSound, activateEvenIfNotNear);
    }

    @Override
    public void onAttack(StellarEndStageEntity stellar, LivingEntity target ,float amount) {
        double x = stellar.getX();
        double y = stellar.getY();
        double z = stellar.getZ();

        if (stellar.level() instanceof ServerLevel server) {
            final Vec3 _center = new Vec3(x, y, z);
            List<LivingEntity> _entfound = server.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center)
                    .inflate(WAVE_RADIUS), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
            for (LivingEntity entity : _entfound) {
                if (entity != stellar && (int) entity.getY() == (int) stellar.getY()) {
                    entity.hurt(Utils.createDamageSource(server, ModDamageTypes.SONIC_BOOM_WITH_ALL_BYPASS, stellar), amount);
                }
            }
            server.sendParticles(ParticleTypes.EXPLOSION, stellar.getX(), stellar.getY() + 0.5, stellar.getZ(), 64, WAVE_RADIUS, 0, WAVE_RADIUS, 0);
        }
    }
}
