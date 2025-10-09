package net.flansflame.flans_star_forge.world.block;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.world.block.custom.MeteorBlock;
import net.flansflame.flans_star_forge.world.block.custom.UniStoneBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, FlansStarForge.MOD_ID);

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FlansStarForge.MOD_ID);

    public static final DeferredRegister<Item> NULL_ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FlansStarForge.MOD_ID);

    public static final RegistryObject<Block> METEOR = registerNull("meteor",
            () -> new MeteorBlock(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).mapColor(MapColor.COLOR_LIGHT_BLUE).noLootTable()));

    public static final RegistryObject<Block> UNI_STONE = register("uni_stone",
            () -> new UniStoneBlock(BlockBehaviour.Properties.of().strength(10f, 1200f).sound(SoundType.STONE).requiresCorrectToolForDrops().mapColor(MapColor.COLOR_LIGHT_BLUE).noLootTable()));

    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(id, block);
        ITEMS.register(id, () -> new BlockItem(toReturn.get(), new Item.Properties()));
        return toReturn;
    }
    private static <T extends Block> RegistryObject<T> registerNull(String id, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(id, block);
        NULL_ITEMS.register(id, () -> new BlockItem(toReturn.get(), new Item.Properties()));
        return toReturn;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        NULL_ITEMS.register(eventBus);
    }
}