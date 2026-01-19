package net.flansflame.flans_star_forge.energy;

public enum QuintLongValue {
    ZERO(new QuintLong()),
    ONE(new QuintLong(1L)),
    TEN(new QuintLong(10L)),
    HUNDRED(new QuintLong(100L)),
    THOUSAND(new QuintLong(1000L)),
    MILLION(new QuintLong(1000000L)),
    BILLION(new QuintLong(1000000000L)),
    TRILLION(new QuintLong(1000000000000L)),
    QUADRILLION(new QuintLong(1L, 0)),
    QUINTILLION(new QuintLong(1000L, 0)),
    SEXTILLION(new QuintLong(1000000L, 0)),
    SEPTILLION(new QuintLong(1000000000L, 0)),
    OCTILLION(new QuintLong(1000000000000L, 0)),
    NONILLION(new QuintLong(1L, 0, 0)),
    DECILLION(new QuintLong(1000L, 0, 0)),
    UNDECILLION(new QuintLong(1000000L, 0, 0)),
    DUODECILLION(new QuintLong(1000000000L, 0, 0)),
    TREDECILLION(new QuintLong(1000000000000L, 0, 0)),
    QUATTUORDECILLION(new QuintLong(1L, 0, 0, 0)),
    QUINDECILLION(new QuintLong(1000L, 0, 0, 0)),
    SEDECILLION(new QuintLong(1000000L, 0, 0, 0)),
    SEPTENDECILLION(new QuintLong(1000000000L, 0, 0, 0)),
    OCTODECILLION(new QuintLong(1000000000000L, 0, 0, 0)),
    NOVENDECILLION(new QuintLong(1L, 0, 0, 0, 0)),
    VIGINTILLION(new QuintLong(1000L, 0, 0, 0, 0)),
    UNVIGINTILLION(new QuintLong(1000000L, 0, 0, 0, 0)),
    DUOVIGINTILLION(new QuintLong(1000000000L, 0, 0, 0, 0)),
    TRESVIGINTILLION(new QuintLong(1000000000000L, 0, 0, 0, 0)),
    QUATTUORVIGINTILLION(new QuintLong(1000000000000000L, 0, 0, 0, 0)),
    QUINVIGINTILLION(new QuintLong(1000000000000000000L, 0, 0, 0, 0));

    private final QuintLong quintLong;

    QuintLongValue(QuintLong quintLong) {
        this.quintLong = quintLong;
    }

    public QuintLong get() {
        return quintLong.copy();
    }
}
