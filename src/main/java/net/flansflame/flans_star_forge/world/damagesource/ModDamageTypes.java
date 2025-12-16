package net.flansflame.flans_star_forge.world.damagesource;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> MAGIC_WITH_COOLDOWN_BYPASS = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(FlansStarForge.MOD_ID, "magic_with_cooldown_bypass"));
    public static final ResourceKey<DamageType> SONIC_BOOM_WITH_COOLDOWN_BYPASS = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(FlansStarForge.MOD_ID, "sonic_boom_with_cooldown_bypass"));
}