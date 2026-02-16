package net.flansflame.flans_star_forge.energy;

import com.electronwill.nightconfig.core.conversion.InvalidValueException;
import org.jetbrains.annotations.NotNull;

public final class QuintLong {

    private static final long V0_MAX_VALUE = Long.MAX_VALUE;
    private static final long V1_V4_MAX_VALUE = 9999999999999999L;
    private static final long MIN_VALUE = 0L;

    public static final QuintLong MAX_VALUE = new QuintLong(V0_MAX_VALUE, V1_V4_MAX_VALUE, V1_V4_MAX_VALUE, V1_V4_MAX_VALUE, V1_V4_MAX_VALUE);
    public static final int LAYER_SIZE = 5;

    private final long[] value;

    public QuintLong() {
        this(MIN_VALUE, MIN_VALUE, MIN_VALUE, MIN_VALUE, MIN_VALUE);
    }

    public QuintLong(long value4) {
        this(MIN_VALUE, MIN_VALUE, MIN_VALUE, MIN_VALUE, value4);
    }

    public QuintLong(long value3, long value4) {
        this(MIN_VALUE, MIN_VALUE, MIN_VALUE, value3, value4);
    }

    public QuintLong(long value2, long value3, long value4) {
        this(MIN_VALUE, MIN_VALUE, value2, value3, value4);
    }

    public QuintLong(long value1, long value2, long value3, long value4) {
        this(MIN_VALUE, value1, value2, value3, value4);
    }

    //memo
    //9,223,372,036,854,775,808
    //8,765,432,098,765,432,210

    //410,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000
    //value0                  value1              value2              value3              value4
    public QuintLong(long value0, long value1, long value2, long value3, long value4) {
        if (value0 < 0) {
            throw new InvalidValueException("value" + 0 + " has to be in between " + MIN_VALUE + " and " + V0_MAX_VALUE + ". That value is invalid.");
        }
        if (value1 > V1_V4_MAX_VALUE || value1 < 0) {
            throw new InvalidValueException("value" + 1 + " has to be in between " + MIN_VALUE + " and " + V1_V4_MAX_VALUE + ". That value is invalid.");
        }
        if (value2 > V1_V4_MAX_VALUE || value2 < 0) {
            throw new InvalidValueException("value" + 2 + " has to be in between " + MIN_VALUE + " and " + V1_V4_MAX_VALUE + ". That value is invalid.");
        }
        if (value3 > V1_V4_MAX_VALUE || value3 < 0) {
            throw new InvalidValueException("value" + 3 + " has to be in between " + MIN_VALUE + " and " + V1_V4_MAX_VALUE + ". That value is invalid.");
        }
        if (value4 > V1_V4_MAX_VALUE || value4 < 0) {
            throw new InvalidValueException("value" + 4 + " has to be in between " + MIN_VALUE + " and " + V1_V4_MAX_VALUE + ". That value is invalid.");
        }

        this.value = new long[]{value0, value1, value2, value3, value4};
    }

    public long[] getLayer(){
        long[] layers = new long[5];

        for (int i = 0; i < this.value.length; i++) {
            layers[i] = this.getLayer(i);
        }

        return layers;
    }

    public void set(@NotNull QuintLong quintLong) {
        System.arraycopy(quintLong.value, 0, this.value, 0, value.length);
    }

    public void set() {
        this.set(new QuintLong());
    }

    public QuintLong add(@NotNull QuintLong quintLong) {
        for (int i = 0; i < this.value.length; i++) {
            this.addLayer(i, quintLong.value[i]);
        }
        return this;
    }

    public QuintLong add(long l) {
        return this.add(new QuintLong(l));
    }

    public QuintLong add() {
        return this.add(new QuintLong(1));
    }

    public QuintLong remove(@NotNull QuintLong quintLong) {
        for (int i = 0; i < this.value.length; i++) {
            this.removeLayer(i, quintLong.value[i]);
        }
        return this;
    }

    public QuintLong remove(long l) {
        return this.remove(new QuintLong(l));
    }

    public QuintLong remove() {
        return this.remove(new QuintLong(1));
    }

    public QuintLong divide(@NotNull QuintLong quintLong) {
        for (int i = 4; i >= 0; i--) {
            this.divideLayer(i, quintLong);
        }
        return this;
    }

    public QuintLong divide(long l) {
        return this.divide(new QuintLong(l));
    }

    public float divideAndGetFloat(@NotNull QuintLong quintLong) {
        for (int i = 0; i < this.value.length; i++) {
            if (quintLong.value[i] != 0) {
                return this.divideLayerAndGetFloat(i, quintLong);
            }
        }

        throw new InvalidValueException("Cannot divide with a zero.");
    }

    public float divideAndGetFloat(long l) {
        return this.divideAndGetFloat(new QuintLong(l));
    }

    public void setLayer(int layer, long set) {
        if (layer > this.value.length - 1 || layer < 0) {
            throw new InvalidValueException("layer has to be in between " + 0 + " and " + (this.value.length - 1) + ". That value is invalid.");
        }

        if (layer != 0 && (set > V1_V4_MAX_VALUE || set < 0)) {
            throw new InvalidValueException("value" + layer + " has to be in between " + MIN_VALUE + " and " + V1_V4_MAX_VALUE + ". That value is invalid.");
        } else {
            if (set < 0) {
                throw new InvalidValueException("value" + layer + " has to be in between " + MIN_VALUE + " and " + V0_MAX_VALUE + ". That value is invalid.");
            }
        }

        this.value[layer] = set;
    }

    public long getLayer(int layer) {
        if (layer > this.value.length - 1 || layer < 0) {
            throw new InvalidValueException("layer has to be in between " + 0 + " and " + (this.value.length - 1) + ". That value is invalid.");
        }
        return this.value[layer];
    }

    public boolean addLayer(int layer, long add) {
        if (layer > this.value.length - 1 || layer < 0) {
            throw new InvalidValueException("layer has to be in between " + 0 + " and " + (this.value.length - 1) + ". That value is invalid.");
        }
        if (layer != 0 && (add > V1_V4_MAX_VALUE || add < 0)) {
            throw new InvalidValueException("value" + layer + " has to be in between " + MIN_VALUE + " and " + V1_V4_MAX_VALUE + ". That value is invalid.");
        } else {
            if (add < 0) {
                throw new InvalidValueException("value" + layer + " has to be in between " + MIN_VALUE + " and " + V0_MAX_VALUE + ". That value is invalid.");
            }
        }

        if (layer == 0) {
            try {
                this.value[layer] += add;
            } catch (Exception e) {
                return false;
            }
        } else {
            long added = this.value[layer] + add;
            if (added > V1_V4_MAX_VALUE) {
                added -= V1_V4_MAX_VALUE + 1;

                if (this.addLayer(layer - 1)) return false;
            }
            this.value[layer] = added;
        }
        return true;
    }

    public boolean addLayer(int layer) {
        return this.addLayer(layer, 1);
    }

    public boolean removeLayer(int layer, long remove) {
        if (layer > this.value.length - 1 || layer < 0) {
            throw new InvalidValueException("layer has to be in between " + 0 + " and " + (this.value.length - 1) + ". That value is invalid.");
        }
        if (layer != 0) {
            if (remove > V1_V4_MAX_VALUE || remove < 0) {
                throw new InvalidValueException("value" + layer + " has to be in between " + MIN_VALUE + " and " + V1_V4_MAX_VALUE + ". That value is invalid.");
            }
        } else {
            if (remove < 0) {
                throw new InvalidValueException("value" + layer + " has to be in between " + MIN_VALUE + " and " + V0_MAX_VALUE + ". That value is invalid.");
            }
        }

        long removed = this.value[layer] - remove;

        if (layer == 0) {
            if (removed < 0) {
                this.value[layer] = 0;
                return false;
            }
        } else {
            if (removed < 0) {
                if (!this.removeLayer(layer - 1)) {
                    this.value[layer] = 0;
                    return false;
                }
                removed += (V1_V4_MAX_VALUE + 1) / 10;
            }
        }

        this.value[layer] = removed;
        return true;
    }

    public boolean removeLayer(int layer) {
        return this.removeLayer(layer, 1);
    }

    public void divideLayer(int layer, QuintLong divide) {

        if (divide.is(QuintLongValue.ZERO.get())) {
            throw new InvalidValueException("Cannot divide with a zero.");
        }

        if (layer > this.value.length - 1 || layer < 0) {
            throw new InvalidValueException("layer has to be in between " + 0 + " and " + (this.value.length - 1) + ". That value is invalid.");
        }

        if (this.value[layer] == 0) return;

        if (layer == this.value.length - 1) {
            this.value[layer] = (long) this.divideAndGetFloat(divide);
        } else {
            float divided = (long) this.divideAndGetFloat(divide);
            if (divided >= 1) {
                this.value[layer] = (long) divided;
            } else {
                long modDivided = (long) (divided * ((float) (V1_V4_MAX_VALUE + 1) / 10));
                this.addLayer(layer + 1, modDivided);
            }
        }
    }

    public void divideLayer(int layer, long divide) {
        this.divideLayer(layer, new QuintLong(divide));
    }

    public float divideLayerAndGetFloat(int layer, QuintLong divide) {
        if (divide.is(QuintLongValue.ZERO.get())) {
            throw new InvalidValueException("Cannot divide with a zero.");
        }

        if (layer > this.value.length - 1 || layer < 0) {
            throw new InvalidValueException("layer has to be in between " + 0 + " and " + (this.value.length - 1) + ". That value is invalid.");
        }

        float value = this.value[layer];

        for (long l : divide.value) {
            if (l == 0) continue;

            value /= l;
        }

        return value;
    }

    public float divideLayerAndGetFloat(int layer, long divide) {
        return this.divideLayerAndGetFloat(layer, new QuintLong(divide));
    }

    public boolean is(@NotNull QuintLong quad) {
        boolean match = true;
        for (int i = 0; i < value.length; i++) {
            if (this.value[i] != quad.value[i]) {
                match = false;
                break;
            }
        }

        return match;
    }

    public boolean isGreaterThan(@NotNull QuintLong quintLong) {
        for (int i = 0; i < this.value.length; i++) {
            if (this.value[i] == 0 && quintLong.value[i] == 0) {
                continue;
            }
            return this.value[i] > quintLong.value[i];
        }
        return false;
    }

    public boolean isGreaterThan(long l) {
        return this.isGreaterThan(new QuintLong(l));
    }

    public boolean isSmallerThan(@NotNull QuintLong quintLong) {
        for (int i = 0; i < this.value.length; i++) {
            if (this.value[i] == 0 && quintLong.value[i] == 0) {
                continue;
            }
            return this.value[i] < quintLong.value[i];
        }
        return false;
    }

    public boolean isSmallerThan(long l) {
        return this.isSmallerThan(new QuintLong(l));
    }

    public boolean isGreaterOrSameThan(@NotNull QuintLong quintLong) {
        return this.is(quintLong) || this.isGreaterThan(quintLong);
    }

    public boolean isGreaterOrSameThan(long l) {
        return this.isGreaterOrSameThan(new QuintLong(l));
    }

    public boolean isSmallerOrSameThan(@NotNull QuintLong quintLong) {
        return this.is(quintLong) || this.isSmallerThan(quintLong);
    }

    public boolean isSmallerOrSameThan(long l) {
        return this.isSmallerOrSameThan(new QuintLong(l));
    }

    public QuintLong copy() {
        return new QuintLong(this.value[0], this.value[1], this.value[2], this.value[3], this.value[4]);
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        String[] layers = {"", "", "", "", ""};

        if (this.value[0] != 0) {
            layers[0] = toString(this.value[0]);
        }

        for (int i = 1; i < this.value.length; i++) {
            if (this.value[i] != 0) {
                if (layers[i - 1].isEmpty()) {
                    layers[i] = toString(this.value[i]);
                } else {
                    layers[i] = addExtraZero(toString(this.value[i]));
                }
            } else {
                if (!layers[i - 1].isEmpty()) {
                    layers[i] = addExtraZero("0");
                }
            }
        }

        for (String layer : layers) {
            toReturn.append(layer);
        }

        String strToReturn = toReturn.toString();

        if (strToReturn.isEmpty()) {
            strToReturn = "0";
        }

        return addComma(strToReturn);
    }

    public Integer toInteger() {
        if (this.isGreaterThan(new QuintLong(Integer.MAX_VALUE))) return Integer.MAX_VALUE;
        if (this.value[4] > Integer.MAX_VALUE) return Integer.MAX_VALUE;

        return (int) this.value[4];
    }

    public static QuintLong createFromList(long[] layers) {
        QuintLong toReturn = new QuintLong();

        if (layers == null || layers.length > LAYER_SIZE) return toReturn;

        for (int i = 0; i < LAYER_SIZE; i++) {
            toReturn.setLayer(i, layers[i]);
        }
        return toReturn;
    }

    private static String toString(Long l) {
        return l.toString();
    }

    private static String addExtraZero(String string) {
        if (string.isEmpty()) string = "0";

        StringBuilder toReturn = new StringBuilder();

        final int maxCharacter = 15;
        char[] characters = string.toCharArray();

        toReturn.append("0".repeat(Math.max(0, maxCharacter - characters.length)));
        toReturn.append(characters);

        return toReturn.toString();
    }

    public static String addComma(String string) {

        if (string.length() < 4) return string;

        StringBuilder toReturn = new StringBuilder();

        char[] characters = string.toCharArray();
        int types = string.length() % 3;
        int offset = switch (types) {
            case 0 -> 1;
            case 1 -> 3;
            case 2 -> 2;
            default -> throw new IllegalStateException("Unexpected value: " + types);
        };

        for (int i = 0; i < characters.length; i++) {
            toReturn.append(characters[i]);
            if ((i + offset) % 3 == 0 && i != characters.length - 1) {
                toReturn.append(",");
            }
        }

        return toReturn.toString();
    }
}