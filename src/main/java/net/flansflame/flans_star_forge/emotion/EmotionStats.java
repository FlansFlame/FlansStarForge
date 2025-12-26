package net.flansflame.flans_star_forge.emotion;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class EmotionStats {
    public static final int MAX_STATS = 8;
    public static final int DEFAULT_STATS = 6;

    public static int behaviorRates2Amount(EmotionBehaviorRates behaviorRates){
        int amount = 0;

        if (Mth.nextInt(RandomSource.create(), 1, 100) <= behaviorRates.percentage){
            amount = behaviorRates.amount;
        }
        return amount;
    }

    public static int makeValid(int status){
        int modStatus = status;

        if (modStatus < 0){
            modStatus = 0;
        } else if (modStatus > EmotionStats.MAX_STATS) {
            modStatus = EmotionStats.MAX_STATS;
        }
        return modStatus;
    }
}
