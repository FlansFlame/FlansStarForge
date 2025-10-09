package net.flansflame.flans_star_forge.client.keys;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.event.KeyEvent;

@Mod.EventBusSubscriber(modid = FlansStarForge.MOD_ID,bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModKeyBindings {

    public static final KeyMapping[] keys = new KeyMapping[1];

    @SubscribeEvent
    public static void registerKeys(final RegisterKeyMappingsEvent event){
        keys[0] = create("teleport_stellar", KeyEvent.VK_I);

        for (KeyMapping key : keys) {
            event.register(key);
        }
    }

    private static KeyMapping create(String name, int key) {
        return new KeyMapping("key." + FlansStarForge.MOD_ID + "." + name, key, "key.categories." + FlansStarForge.MOD_ID);
    }
}
