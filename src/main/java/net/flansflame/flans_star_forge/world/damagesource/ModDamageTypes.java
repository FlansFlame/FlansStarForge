package net.flansflame.flans_star_forge.world.damagesource;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public interface ModDamageTypes {
    ResourceKey<DamageType> MAGIC_WITH_ALL_BYPASS = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(FlansStarForge.MOD_ID, "magic_with_all_bypass"));
    ResourceKey<DamageType> SONIC_BOOM_WITH_ALL_BYPASS = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(FlansStarForge.MOD_ID, "sonic_boom_with_all_bypass"));
    ResourceKey<DamageType> MOB_ATTACK_WITH_ALL_BYPASS = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(FlansStarForge.MOD_ID, "mob_attack_with_all_bypass"));
    ResourceKey<DamageType> EXPLOSION_WITH_ALL_BYPASS = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(FlansStarForge.MOD_ID, "explosion_with_all_bypass"));
    ResourceKey<DamageType> WITHERING_WITH_ALL_WITHOUT_COOLDOWN_BYPASS = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(FlansStarForge.MOD_ID, "withering_with_all_without_cooldown_bypass"));
}