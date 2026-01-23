package net.flansflame.flans_star_forge.entities.ai.end_stellar.custom;

import net.flansflame.flans_knowledge_lib.Utils;
import net.flansflame.flans_star_forge.entities.ai.end_stellar.EndStellarAttackPhase;
import net.flansflame.flans_star_forge.event.MobStrengthenEvents;
import net.flansflame.flans_star_forge.damagesource.ModDamageTypes;
import net.flansflame.flans_star_forge.entities.entity.StellarEndStageEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class ExplodeEndStellarAttack extends EndStellarAttackPhase {

    private static final float EXPLODING_OMEN_DAMAGE_DIVIDER = 8f;
    private static final float EXPLOSION_DAMAGE_DIVIDER = 0.5f;
    private static final int EXPLOSION_RADIUS = 5;

    public ExplodeEndStellarAttack(String animationId, SoundEvent attackSound, boolean activateEvenIfNotNear) {
        super(animationId, attackSound, activateEvenIfNotNear);
    }

    @Override
    public void onAttack(StellarEndStageEntity stellar, LivingEntity target, float amount) {
        double x = stellar.getX();
        double y = stellar.getY();
        double z = stellar.getZ();

        if (stellar.level() instanceof ServerLevel server) {
            final Vec3 _center = new Vec3(x, y, z);
            List<LivingEntity> _entfound = server.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center)
                    .inflate(EXPLOSION_RADIUS), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
            for (LivingEntity entity : _entfound) {
                if (entity != stellar) {
                    entity.hurt(Utils.createDamageSource(server, ModDamageTypes.EXPLOSION_WITH_ALL_BYPASS, stellar), amount / EXPLOSION_DAMAGE_DIVIDER);
                }
            }
            server.sendParticles(ParticleTypes.EXPLOSION_EMITTER, stellar.getX(), stellar.getY() + 0.5, stellar.getZ(), 4, 3, 0, 3, 0);
            server.playSound(null, stellar.blockPosition(), this.getAttackSound(), SoundSource.HOSTILE);
        }
    }

    @Override
    public void beforeAttack(StellarEndStageEntity stellar, LivingEntity target) {
        double x = stellar.getX();
        double y = stellar.getY();
        double z = stellar.getZ();

        if (stellar.level() instanceof ServerLevel server) {
            final Vec3 _center = new Vec3(x, y, z);
            List<LivingEntity> _entfound = server.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center)
                    .inflate(EXPLOSION_RADIUS), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
            for (LivingEntity entity : _entfound) {
                if (entity != stellar) {
                    entity.hurt(new DamageSource(server.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.CRAMMING)), stellar.getAttackDamage() / EXPLODING_OMEN_DAMAGE_DIVIDER);
                }
            }
        }
        super.beforeAttack(stellar, target);
    }
}
