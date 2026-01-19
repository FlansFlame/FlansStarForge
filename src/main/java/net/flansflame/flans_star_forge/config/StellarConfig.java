package net.flansflame.flans_star_forge.config;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.entities.entity.StellarEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = FlansStarForge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class StellarConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Double> ATTACK_DAMAGE;

    static {
        BUILDER.push(FlansStarForge.MOD_ID + "-stellar");

        ATTACK_DAMAGE = BUILDER.comment("I do not recommend nor support changing these during fight or when spawned. [default:12.0]").define("attack_damage", (double) StellarEntity.ATTACK_DAMAGE);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    public static void registerAndApplyConfigs(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC && CommonConfig.LIGHT_MODE.get()) {
            StellarEntity.ATTACK_DAMAGE = ATTACK_DAMAGE.get().floatValue();
        }
    }
}
