package net.flansflame.flans_star_forge.screens;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.screens.screen.CombinerScreen;
import net.flansflame.flans_star_forge.screens.screen.FE2SdEConverterScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = FlansStarForge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Screen2MenuConnector {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.COMBINER.get(), CombinerScreen::new);
            MenuScreens.register(ModMenuTypes.FE_2_SDE_CONVERTER.get(), FE2SdEConverterScreen::new);
        });
    }
}
