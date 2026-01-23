package net.flansflame.flans_star_forge.config;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.entities.entity.StellarEndStageEntity;
import net.flansflame.flans_star_forge.event.MobStrengthenEvents;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = FlansStarForge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class StarsBlessingConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Double> DEFAULT_ATTACK_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Double> DEFAULT_ATTACKED_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Double> BLESSED_ATTACK_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Double> BLESSED_ATTACKED_MULTIPLIER;


    static {
        BUILDER.push(FlansStarForge.MOD_ID + "-common");

        DEFAULT_ATTACK_MULTIPLIER = BUILDER.comment("[default:" + MobStrengthenEvents.DEFAULT_ATTACK_MULTIPLIER + "]").define("default_attack_multiplier", (double) MobStrengthenEvents.DEFAULT_ATTACK_MULTIPLIER);
        DEFAULT_ATTACKED_MULTIPLIER = BUILDER.comment("[default:" + MobStrengthenEvents.DEFAULT_ATTACKED_MULTIPLIER + "]").define("default_attacked_multiplier", (double) MobStrengthenEvents.DEFAULT_ATTACKED_MULTIPLIER);
        BLESSED_ATTACK_MULTIPLIER = BUILDER.comment("[default:" + MobStrengthenEvents.BLESSED_ATTACK_MULTIPLIER + "]").define("blessed_attack_multiplier", (double) MobStrengthenEvents.BLESSED_ATTACK_MULTIPLIER);
        BLESSED_ATTACKED_MULTIPLIER = BUILDER.comment("[default:" + MobStrengthenEvents.BLESSED_ATTACKED_MULTIPLIER + "]").define("blessed_attacked_multiplier", (double) MobStrengthenEvents.BLESSED_ATTACKED_MULTIPLIER);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    public static void registerAndApplyConfigs(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC && CommonConfig.LIGHT_MODE.get()){
            MobStrengthenEvents.DEFAULT_ATTACK_MULTIPLIER = DEFAULT_ATTACK_MULTIPLIER.get().floatValue();
            MobStrengthenEvents.DEFAULT_ATTACKED_MULTIPLIER = DEFAULT_ATTACKED_MULTIPLIER.get().floatValue();
            MobStrengthenEvents.BLESSED_ATTACK_MULTIPLIER = BLESSED_ATTACK_MULTIPLIER.get().floatValue();
            MobStrengthenEvents.BLESSED_ATTACKED_MULTIPLIER = BLESSED_ATTACKED_MULTIPLIER.get().floatValue();
        }
    }
}
