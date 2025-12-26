package net.flansflame.flans_star_forge.config;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> DISABLE_EMOTION_BAR;

    static {
        BUILDER.push(FlansStarForge.MOD_ID + "-client");

        DISABLE_EMOTION_BAR = BUILDER.comment("[default:false]").define("disable_emotion_bar", false);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
