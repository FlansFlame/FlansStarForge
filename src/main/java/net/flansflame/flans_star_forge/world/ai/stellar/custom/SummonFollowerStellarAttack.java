package net.flansflame.flans_star_forge.world.ai.stellar.custom;

import net.flansflame.flans_star_forge.world.ai.stellar.StellarAttackPhase;
import net.flansflame.flans_star_forge.world.entity.ModEntities;
import net.flansflame.flans_star_forge.world.entity.custom.StellarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class SummonFollowerStellarAttack extends StellarAttackPhase {

    public static final int FOLLOWER_DETECT_RADIUS = 50;
    public static final int MAX_FOLLOWERS = 6;
    public static final int MAX_LOOPS = 20;

    public SummonFollowerStellarAttack(String animationId, SoundEvent attackSound) {
        super(animationId, attackSound);
    }

    @Override
    public void onAttack(StellarEntity stellar, LivingEntity target) {
        double x = stellar.getX();
        double y = stellar.getY();
        double z = stellar.getZ();

        if (stellar.level() instanceof ServerLevel server) {
            int followerCount = 0;

            {
                final Vec3 _center = new Vec3(x, y, z);
                List<LivingEntity> _entfound = server.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center)
                        .inflate(FOLLOWER_DETECT_RADIUS), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (LivingEntity entity : _entfound) {
                    if (entity.getType() == ModEntities.STARS_CLUSTER.get()) {
                        followerCount++;
                    }
                }
            }

            if (followerCount < MAX_FOLLOWERS) {
                for (int i = 0; i < 3; i++) {
                    double vX = x + Mth.nextInt(RandomSource.create(), -4, 4);
                    double vZ = z + Mth.nextInt(RandomSource.create(), -4, 4);

                    int stack = 0;

                    while ((vX == x || vZ == z || server.getBlockState(BlockPos.containing(vX, y, vZ)).canOcclude()) && stack < MAX_LOOPS) {
                        vX = x + Mth.nextInt(RandomSource.create(), -4, 4);
                        vZ = z + Mth.nextInt(RandomSource.create(), -4, 4);
                        stack++;
                    }
                    if (stack >= MAX_LOOPS) {
                        break;
                    }

                    server.sendParticles(ParticleTypes.EXPLOSION_EMITTER, vX, y, vZ, 1, 0, 0, 0, 0);
                    var entityToSpawn = ModEntities.STARS_CLUSTER.get().spawn(server, BlockPos.containing(vX, y, vZ), MobSpawnType.MOB_SUMMONED);
                    if (entityToSpawn != null) {
                        entityToSpawn.setYRot(stellar.getYRot());
                        entityToSpawn.setPos(vX + 0.5, y, vZ + 0.5);
                        entityToSpawn.tame((Player) stellar.getOwner());
                    }
                }
            } else {
                {
                    server.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 64, 0, 0, 0, 0.2);
                    stellar.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 40, 20));
                    stellar.addEffect(new MobEffectInstance(MobEffects.GLOWING, 40, 20));
                }
            }
        }
        super.onAttack(stellar, target);
    }
}