package net.flansflame.flans_star_forge.world.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class Attacks {
    public static void darkBurst(LivingEntity pEntity, double x, double y, double z, ServerLevel server) {
        server.playSound(null, BlockPos.containing(x, y, z), SoundEvents.TRIDENT_RETURN, SoundSource.HOSTILE, 1f, 1f);
        server.sendParticles(ParticleTypes.SCULK_SOUL, x, y + 1, z, 128, 2, 0, 2, 0);

        final Vec3 _center = new Vec3(x, y, z);
        List<LivingEntity> _entfound = server.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center)
                .inflate(4 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
        for (LivingEntity entity : _entfound) {
            if (entity != pEntity) {
                entity.hurt(new DamageSource(server.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.BAD_RESPAWN_POINT)), 20);
            }
        }
    }

    public static void summonFollower(LivingEntity pEntity, double x, double y, double z, ServerLevel server) {
        server.playSound(null, BlockPos.containing(x, y, z), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.HOSTILE, 1f, 1f);

        int followerCount = 0;

        {
            final Vec3 _center = new Vec3(x, y, z);
            List<LivingEntity> _entfound = server.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center)
                    .inflate(100 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
            for (LivingEntity entity : _entfound) {
                if (entity.getType() == EntityType.WITHER_SKELETON) {
                    followerCount++;
                }
            }
        }

        if (followerCount < 6) {
            for (int i = 0; i < 3; i++) {
                double vX = x + Mth.nextInt(RandomSource.create(), -4, 4);
                double vZ = z + Mth.nextInt(RandomSource.create(), -4, 4);

                int stack = 0;

                while ((vX == x || vZ == z || !server.getBlockState(BlockPos.containing(vX, y, vZ)).canOcclude()) && stack < 20) {
                    vX = x + Mth.nextInt(RandomSource.create(), -4, 4);
                    vZ = z + Mth.nextInt(RandomSource.create(), -4, 4);
                    stack++;
                }
                if (stack >= 21) {
                    break;
                }

                server.sendParticles(ParticleTypes.EXPLOSION_EMITTER, vX, y, vZ, 1, 0, 0, 0, 0);
                WitherSkeleton entityToSpawn = EntityType.WITHER_SKELETON.spawn(server, BlockPos.containing(vX, y, vZ), MobSpawnType.COMMAND);
                if (entityToSpawn != null) {
                    entityToSpawn.setYRot(pEntity.getYRot());
                    entityToSpawn.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DIAMOND_SWORD));

                    entityToSpawn.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                }
            }
        } else {
            {
                server.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 64, 0, 0, 0, 0.2);
                pEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 20));
            }
        }
    }
}