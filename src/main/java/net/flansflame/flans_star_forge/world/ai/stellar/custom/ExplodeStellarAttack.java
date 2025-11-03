package net.flansflame.flans_star_forge.world.ai.stellar.custom;

import net.flansflame.flans_star_forge.event.MobStrengthenEvents;
import net.flansflame.flans_star_forge.world.ai.stellar.StellarAttackPhase;
import net.flansflame.flans_star_forge.world.entity.custom.StellarEntity;
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

public class ExplodeStellarAttack extends StellarAttackPhase {

    private static final int EXPLODING_OMEN_DAMAGE = 5;
    private static final int EXPLOSION_DAMAGE = 120;
    private static final int EXPLOSION_RADIUS = 5;

    public ExplodeStellarAttack(String animationId, SoundEvent attackSound) {
        super(animationId, attackSound);
    }

    @Override
    public void onAttack(StellarEntity stellar, LivingEntity target) {
        double x = stellar.getX();
        double y = stellar.getY();
        double z = stellar.getZ();

        if (stellar.level() instanceof ServerLevel server) {
            final Vec3 _center = new Vec3(x, y, z);
            List<LivingEntity> _entfound = server.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center)
                    .inflate(EXPLOSION_RADIUS), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
            for (LivingEntity entity : _entfound) {
                if (entity != stellar && !stellar.isOwnedBy(entity)) {
                    entity.hurt(new DamageSource(server.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.EXPLOSION)), EXPLOSION_DAMAGE * MobStrengthenEvents.BLESSING_ATTACK_MULTIPLIER);
                }
            }
            server.sendParticles(ParticleTypes.EXPLOSION_EMITTER, stellar.getX(), stellar.getY() + 0.5, stellar.getZ(), 4, 3, 0, 3, 0);
            server.playSound(null, stellar.blockPosition(), this.getAttackSound(), SoundSource.HOSTILE);
        }
    }

    @Override
    public void beforeAttack(StellarEntity stellar, LivingEntity target) {
        double x = stellar.getX();
        double y = stellar.getY();
        double z = stellar.getZ();

        if (stellar.level() instanceof ServerLevel server) {
            final Vec3 _center = new Vec3(x, y, z);
            List<LivingEntity> _entfound = server.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center)
                    .inflate(EXPLOSION_RADIUS), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
            for (LivingEntity entity : _entfound) {
                if (entity != stellar && !stellar.isOwnedBy(entity)) {
                    entity.hurt(new DamageSource(server.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.CRAMMING)), EXPLODING_OMEN_DAMAGE * MobStrengthenEvents.BLESSING_ATTACK_MULTIPLIER);
                }
            }
        }
        super.beforeAttack(stellar, target);
    }
}
