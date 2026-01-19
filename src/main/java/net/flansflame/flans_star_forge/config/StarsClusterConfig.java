package net.flansflame.flans_star_forge.config;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.entities.entity.FailedNovaEntity;
import net.flansflame.flans_star_forge.entities.entity.StarsClusterEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = FlansStarForge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class StarsClusterConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Double> ATTACK_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Double> MOVEMENT_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Integer> DEATH_TICK;

    static {
        BUILDER.push(FlansStarForge.MOD_ID + "-stars_cluster");

        ATTACK_DAMAGE = BUILDER.comment("I do not recommend nor support changing these during fight or when spawned. [default:100.0]").define("attack_damage", (double) StarsClusterEntity.ATTACK_DAMAGE);
        MOVEMENT_SPEED = BUILDER.comment("[default:0.6]").define("movement_speed", (double) StarsClusterEntity.MOVEMENT_SPEED);
        DEATH_TICK = BUILDER.comment("[default:600.0]").define("death_tick", StarsClusterEntity.DEATH_TICK);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    public static void registerAndApplyConfigs(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC && CommonConfig.LIGHT_MODE.get()) {
            StarsClusterEntity.ATTACK_DAMAGE = ATTACK_DAMAGE.get().floatValue();
            StarsClusterEntity.MOVEMENT_SPEED = MOVEMENT_SPEED.get().floatValue();
            StarsClusterEntity.DEATH_TICK = DEATH_TICK.get();
        }
    }
}
