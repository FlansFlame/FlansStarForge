package net.flansflame.flans_star_forge.renderers;

import net.flansflame.flans_star_forge.entities.ModEntities;
import net.flansflame.flans_star_forge.renderers.entity.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EntityRenderer {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(ModEntities.FAILED_NOVA.get(), renderManager -> new FailedNovaRenderer<>(renderManager,"the_failed_nova"));

        event.registerEntityRenderer(ModEntities.STELLAR.get(), renderManager -> new StellarRenderer<>(renderManager, "stellar"));
        event.registerEntityRenderer(ModEntities.STARS_CLUSTER.get(), renderManager -> new StarsClusterRenderer<>(renderManager, "null"));

        event.registerEntityRenderer(ModEntities.STELLAR_END_STAGE.get(), renderManager -> new StellarEndStageRenderer<>(renderManager, "stellar_end_stage"));
        event.registerEntityRenderer(ModEntities.WITHER_BOMB.get(), renderManager -> new WitherBombRenderer<>(renderManager, "wither_bomb"));    }
}
