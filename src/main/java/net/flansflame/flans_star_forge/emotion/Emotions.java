package net.flansflame.flans_star_forge.emotion;

import net.flansflame.flans_star_forge.mixin_accesor.IPlayerMixinAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public enum Emotions {
    OMNIPOTENT("command_block_side"),
    EXCITE("redstone_lamp_on"),
    STILL("grass_block_side"),
    ANXIETY("cracked_stone_bricks"),
    PANIC("tnt_side"),
    DESPERATE("packed_ice"),
    INSANE("sculk_shrieker_can_summon_inner_top");

    public final ResourceLocation texture;

    Emotions(String path) {
        texture = new ResourceLocation("minecraft", "textures/block/" + path + ".png");
    }

    public boolean is(Emotions emotions) {
        return this == emotions;
    }

    public static Emotions getEmotions(Player player) {
        return status2Emotion(((IPlayerMixinAccessor) player).flansStarForge$getStats());
    }

    public static Emotions status2Emotion(int[] status) {
        float emotion = 2f;

        /*
        7:  -0.5
        6:  0
        5:  0
        4:  0
        3:  +0.5
        2:  +1
        1:  +1.5
        0:  +2
        */

        for (int stat : status) {
            switch (stat) {
                case 8, 7 -> emotion -= 0.5f;
                case 3 -> emotion += 0.5f;
                case 2 -> emotion += 1f;
                case 1 -> emotion += 1.5f;
                case 0 -> emotion += 2f;
            }
        }
        if (emotion < 0) {
            emotion = 0;
        } else if (emotion >= Emotions.values().length) {
            emotion = Emotions.values().length - 1;
        }

        if (status[0] == 8 && status[1] == 8 && status[2] == 8) {
            return Emotions.OMNIPOTENT;
        }

        return Emotions.values()[(int) emotion];
    }
}