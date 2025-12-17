package net.flansflame.flans_star_forge.config;

import com.mojang.logging.LogUtils;
import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.world.entity.custom.StellarEndStageEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = FlansStarForge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> LIGHT_MODE;

    public static final ForgeConfigSpec.ConfigValue<Double> MAX_HEALTH;
    public static final ForgeConfigSpec.ConfigValue<Double> ATTACK_DAMAGE;

    static {
        BUILDER.push(FlansStarForge.MOD_ID + "-common");

        LIGHT_MODE = BUILDER.comment("Makes the boss slightly weaker & customizable (make the boss use the config and the tag values rather than hardcoding) [default:false]").define("light_mode", false);

        MAX_HEALTH = BUILDER.comment("I do not recommend nor support changing these 2 during fight. [default:4096.0]").define("max_health", (double) StellarEndStageEntity.MAX_EX_HP);
        ATTACK_DAMAGE = BUILDER.comment("[default:40.0]").define("attack_damage", (double) StellarEndStageEntity.ATTACK_DAMAGE);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    public static void registerAndApplyConfigs(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC && LIGHT_MODE.get()){
            StellarEndStageEntity.MAX_EX_HP = MAX_HEALTH.get().floatValue();
            StellarEndStageEntity.ATTACK_DAMAGE = ATTACK_DAMAGE.get().floatValue();
        }
    }
}
