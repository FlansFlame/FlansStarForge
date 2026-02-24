package net.flansflame.flans_star_forge.items;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.items.item.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FlansStarForge.MOD_ID);

    public static final RegistryObject<Item> STARS_POWERSTONE = ITEMS.register("stars_powerstone",
            () -> new StarsPowerStoneItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(1)));

    public static final RegistryObject<Item> WITHERING_POWERSTONE = ITEMS.register("withering_powerstone",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> FORGED_STARS_FRAGMENT = ITEMS.register("forged_stars_fragment",
            () -> new ForgedStarsFragmentSword(8, 1.8f, new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));

    public static final RegistryObject<Item> STARS_FRAGMENT = ITEMS.register("stars_fragment",
            () -> new StarsFragmentSword(28, 2.4f, new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).fireResistant()));

    public static final RegistryObject<Item> MYSTERIOUS_MECHANISM = ITEMS.register("mysterious_mechanism", createSimpleItem());
    public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench",
            () -> new WrenchItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ENERGIZED_CLOCK = ITEMS.register("energized_clock",
            () -> new EnergizedClockItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PRESSURIZED_CLOCK = ITEMS.register("pressurized_clock",
            () -> new PressurizedClockItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> REFINED_AMETHYST_SHARD = ITEMS.register("refined_amethyst_shard",
            () -> new FoilItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> INCOMPLETE_BEACON_OF_STAR = ITEMS.register("incomplete_beacon_of_star",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static Supplier<Item> createSimpleItem(){
        return () -> new Item(new Item.Properties());
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}