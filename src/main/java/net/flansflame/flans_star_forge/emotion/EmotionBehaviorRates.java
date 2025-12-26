package net.flansflame.flans_star_forge.emotion;

public enum EmotionBehaviorRates {
    EXCELLENT(80, 3),
    GREAT(50, 2),
    APPROVED(30, 1),
    COOL(10, 1),
    CONTINUOUS(1, -1),
    NOT_COOl(10, -1),
    TOUGH(30, -1),
    OUTLAW(50, -2),
    CENSORED(80, -3);

    public final int percentage;
    public final int amount;

    EmotionBehaviorRates(int percentage, int amount) {
        this.percentage = percentage;
        this.amount = amount;
    }
}