package net.flansflame.flans_star_forge.entities;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.entities.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FlansStarForge.MOD_ID);


    public static final RegistryObject<EntityType<FailedNovaEntity>> FAILED_NOVA =
            ENTITIES.register("failed_nova",
                    () -> EntityType.Builder.of(FailedNovaEntity::new, MobCategory.MISC)
                            .sized(0.8f, 3.8f)
                            .build(new ResourceLocation(FlansStarForge.MOD_ID, "failed_nova").toString()));

    public static final RegistryObject<EntityType<FriendEntity>> FRIEND =
            ENTITIES.register("friend",
                    () -> EntityType.Builder.of(FriendEntity::new, MobCategory.MISC)
                            .sized(0.8f, 0.5f)
                            .build(new ResourceLocation(FlansStarForge.MOD_ID, "friend").toString()));


    public static final RegistryObject<EntityType<StellarEntity>> STELLAR =
            ENTITIES.register("stellar",
                    () -> EntityType.Builder.of(StellarEntity::new, MobCategory.MISC)
                            .sized(0.5f, 2.6f)
                            .build(new ResourceLocation(FlansStarForge.MOD_ID, "stellar").toString()));

    public static final RegistryObject<EntityType<StarsClusterEntity>> STARS_CLUSTER =
            ENTITIES.register("stars_cluster",
                    () -> EntityType.Builder.of(StarsClusterEntity::new, MobCategory.MISC)
                            .sized(0.5f, 1.9f)
                            .build(new ResourceLocation(FlansStarForge.MOD_ID, "stars_cluster").toString()));


    public static final RegistryObject<EntityType<StellarEndStageEntity>> STELLAR_END_STAGE =
            ENTITIES.register("stellar_end_stage",
                    () -> EntityType.Builder.of(StellarEndStageEntity::new, MobCategory.MISC)
                            .sized(0.5f, 2.6f)
                            .build(new ResourceLocation(FlansStarForge.MOD_ID, "stellar_end_stage").toString()));

    public static final RegistryObject<EntityType<WitherBombEntity>> WITHER_BOMB =
            ENTITIES.register("wither_bomb",
                    () -> EntityType.Builder.of(WitherBombEntity::new, MobCategory.MISC)
                            .sized(1f, 1f)
                            .build(new ResourceLocation(FlansStarForge.MOD_ID, "wither_bomb").toString()));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(FAILED_NOVA.get(), FailedNovaEntity.createAttributes().build());
        event.put(FRIEND.get(), FriendEntity.createAttributes().build());

        event.put(STELLAR.get(), StellarEntity.createAttributes().build());
        event.put(STARS_CLUSTER.get(), StarsClusterEntity.createAttributes().build());

        event.put(STELLAR_END_STAGE.get(), StellarEndStageEntity.createAttributes().build());
        event.put(WITHER_BOMB.get(), WitherBombEntity.createAttributes().build());
    }
}