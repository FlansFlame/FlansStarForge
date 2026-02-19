package net.flansflame.flans_star_forge.config;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.entities.ai.failed_nova.passives.passive.KillAuraPassiveSkill;
import net.flansflame.flans_star_forge.entities.entity.FailedNovaEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = FlansStarForge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FailedNovaConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Double> MAX_HEALTH;
    public static final ForgeConfigSpec.ConfigValue<Double> ATTACK_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Integer> PASSIVE_SKILL_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Integer> KILL_AURA_USE_TIME;

    static {
        BUILDER.push(FlansStarForge.MOD_ID + "-failed_nova");

        MAX_HEALTH = BUILDER.comment("I do not recommend nor support changing these during fight or when spawned. [default:2048.0]").define("max_health", (double) FailedNovaEntity.MAX_EX_HP);
        ATTACK_DAMAGE = BUILDER.comment("[default:20.0]").define("attack_damage", (double) FailedNovaEntity.ATTACK_DAMAGE);
        PASSIVE_SKILL_RADIUS = BUILDER.comment("[default:64]").define("passive_skill_radius", FailedNovaEntity.PASSIVE_SKILL_RADIUS);
        KILL_AURA_USE_TIME =  BUILDER.comment("[default:100(tick)]").define("kill_aura_use_time", KillAuraPassiveSkill.KILL_AURA_USE_TIME);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    public static void registerAndApplyConfigs(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC && CommonConfig.LIGHT_MODE.get()) {
            FailedNovaEntity.MAX_EX_HP = MAX_HEALTH.get().floatValue();
            FailedNovaEntity.ATTACK_DAMAGE = ATTACK_DAMAGE.get().floatValue();
            FailedNovaEntity.PASSIVE_SKILL_RADIUS = PASSIVE_SKILL_RADIUS.get();
            KillAuraPassiveSkill.KILL_AURA_USE_TIME = KILL_AURA_USE_TIME.get();
        }
    }
}
