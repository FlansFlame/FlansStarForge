package net.flansflame.flans_star_forge.config;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.entities.entity.StellarEntity;
import net.flansflame.flans_star_forge.entities.entity.WitherBombEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = FlansStarForge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WitherBombConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Double> EXPLOSION_RADIUS;

    static {
        BUILDER.push(FlansStarForge.MOD_ID + "-wither_bomb");

        EXPLOSION_RADIUS = BUILDER.comment("I do not recommend nor support changing these during fight or when spawned. [default:128.0]").define("explosion_radius", (double) WitherBombEntity.EXPLOSION_RADIUS);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    public static void registerAndApplyConfigs(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC && CommonConfig.LIGHT_MODE.get()) {
            WitherBombEntity.EXPLOSION_RADIUS = EXPLOSION_RADIUS.get().floatValue();
        }
    }
}
