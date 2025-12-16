package net.flansflame.flans_star_forge;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;

public class Utils {

    public static DamageSource createDamageSource(ServerLevel server, ResourceKey<DamageType> damageType) {
        return new DamageSource(server.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageType));
    }

    public static DamageSource createDamageSource(ServerLevel server, ResourceKey<DamageType> damageType, LivingEntity entity) {
        return new DamageSource(server.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageType), entity);
    }
}
