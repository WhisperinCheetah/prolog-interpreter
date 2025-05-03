package src.simples;

import java.util.Objects;

public class Number extends Constant<Double> {
    public Number(double value) {
        super(value);
    }

    public static Number fromString(String line) {
        return new Number(Double.parseDouble(line));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Number otherNumber) {
            return Objects.equals(value, otherNumber.value);
        }

        return false;
    }

    @Override
    public Number copy() {
        return new Number(value);
    }
}
