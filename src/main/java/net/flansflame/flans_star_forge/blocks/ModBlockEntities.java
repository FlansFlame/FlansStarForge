package net.flansflame.flans_star_forge.blocks;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.blocks.entity.CombinerBlockEntity;
import net.flansflame.flans_star_forge.blocks.entity.DestructorBlockEntity;
import net.flansflame.flans_star_forge.blocks.entity.FE2SdEConverterBlockEntity;
import net.flansflame.flans_star_forge.blocks.entity.ReforgerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FlansStarForge.MOD_ID);


    public static final RegistryObject<BlockEntityType<FE2SdEConverterBlockEntity>> FE_2_SDE_CONVERTER =
            BLOCK_ENTITIES.register("fe2sde_converter", () ->
                    BlockEntityType.Builder.of(FE2SdEConverterBlockEntity::new,
                            ModBlocks.FE_2_SDE_CONVERTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<CombinerBlockEntity>> COMBINER =
            BLOCK_ENTITIES.register("combiner", () ->
                    BlockEntityType.Builder.of(CombinerBlockEntity::new,
                            ModBlocks.COMBINER.get()).build(null));

    public static final RegistryObject<BlockEntityType<DestructorBlockEntity>> DESTRUCTOR =
            BLOCK_ENTITIES.register("destructor", () ->
                    BlockEntityType.Builder.of(DestructorBlockEntity::new,
                            ModBlocks.DESTRUCTOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<ReforgerBlockEntity>> REFORGER =
            BLOCK_ENTITIES.register("reforger", () ->
                    BlockEntityType.Builder.of(ReforgerBlockEntity::new,
                            ModBlocks.REFORGER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
