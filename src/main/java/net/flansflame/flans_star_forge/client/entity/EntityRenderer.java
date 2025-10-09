package net.flansflame.flans_star_forge.client.entity;

import net.flansflame.flans_star_forge.world.entity.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EntityRenderer {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(ModEntities.STELLAR.get(), renderManager -> new StellarRenderer<>(renderManager, "stellar"));

        event.registerEntityRenderer(ModEntities.STELLAR_END_STAGE.get(), renderManager -> new StellarEndStageRenderer<>(renderManager, "stellar_end_stage"));
    }
}
