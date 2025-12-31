package net.flansflame.flans_star_forge.world.entity;

public interface IHasEmotion {
    default int[] flansStarForge$getStats() {
        return new int[]{this.flansStarForge$getSanity(), this.flansStarForge$getFatigue(), this.flansStarForge$getMotivation()};
    }

    default void flansStarForge$setStats(int[] stats) {
        this.flansStarForge$setSanity(stats[0]);
        this.flansStarForge$setFatigue(stats[1]);
        this.flansStarForge$setMotivation(stats[2]);
    }

    int flansStarForge$getSanity();

    int flansStarForge$getFatigue();

    int flansStarForge$getMotivation();

    void flansStarForge$setSanity(int sanity);

    void flansStarForge$setFatigue(int fatigue);

    void flansStarForge$setMotivation(int motivation);

    default void addSanity(int add) {
        this.flansStarForge$setSanity(this.flansStarForge$getSanity() + add);
    }

    default void addSanity() {
        this.addSanity(1);
    }

    default void addFatigue(int add) {
        this.flansStarForge$setFatigue(this.flansStarForge$getFatigue() + add);
    }

    default void addFatigue() {
        this.addFatigue(1);
    }

    default void addMotivation(int add) {
        this.flansStarForge$setMotivation(this.flansStarForge$getMotivation() + add);
    }

    default void addMotivation() {
        this.addMotivation(1);
    }
}