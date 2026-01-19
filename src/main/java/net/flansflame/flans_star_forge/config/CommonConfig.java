package net.flansflame.flans_star_forge.config;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> LIGHT_MODE;

    static {
        BUILDER.push(FlansStarForge.MOD_ID + "-common");

        LIGHT_MODE = BUILDER.comment("Makes the boss slightly weaker & customizable (make the boss use the config and the tag values rather than hardcoding) [default:false]").define("light_mode", false);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
