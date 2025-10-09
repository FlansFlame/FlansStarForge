package net.flansflame.flans_star_forge.world.effect;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.world.effect.custom.StarsBlessingEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, FlansStarForge.MOD_ID);

    public static final RegistryObject<MobEffect> STARS_BLESSING = EFFECTS.register("stars_blessing", StarsBlessingEffect::new);

    public static void register(IEventBus eventBus){
        EFFECTS.register(eventBus);
    }
}