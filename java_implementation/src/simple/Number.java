package src.simple;

import src.Substitution;
import src.Term;
import src.TermType;

public class Number extends SimpleTerm {
    double value;

    public Number(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static boolean isNumber(String input) {
        return input.matches("-?\\d+(\\.\\d+)?");
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Number otherNumber) {
            return value == otherNumber.value;
        }

        return false;
    }

    @Override
    public Substitution unify(Term other) {
        if (other instanceof Number num && value == num.value) {
            return Substitution.success();
        }

        return Substitution.failure();
    }

    @Override
    public Term substituteVariables(Substitution substitution) {
        return this.copy();
    }

    @Override
    public Number copy() {
        return new Number(value);
    }

    @Override
    public TermType getTermType() {
        return TermType.NUMBER;
    }
}
