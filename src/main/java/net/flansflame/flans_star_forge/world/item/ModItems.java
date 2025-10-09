package net.flansflame.flans_star_forge.world.item;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.world.item.custom.StarsPowerStoneItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FlansStarForge.MOD_ID);

    public static final RegistryObject<Item> STARS_POWERSTONE = ITEMS.register("stars_powerstone",
            () -> new StarsPowerStoneItem(new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> WITHERING_POWERSTONE = ITEMS.register("withering_powerstone",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
