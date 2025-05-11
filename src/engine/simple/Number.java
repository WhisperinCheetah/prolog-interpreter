package engine.simple;

import engine.Substitution;
import engine.Term;
import engine.TermType;
import engine.complex.expression.BinaryExpression;
import engine.complex.expression.EvaluableExpression;

import java.util.HashMap;

public class Number extends SimpleTerm implements EvaluableExpression {
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
        if (other instanceof Variable) return other.unify(this);
        if (other instanceof Number num && value == num.value) return Substitution.success();

        return Substitution.failure();
    }

    @Override
    public Term renameVariables(HashMap<String, Variable> map) {
        return this.copy();
    }

    @Override
    public Term substituteVariables(Substitution substitution) {
        return this.copy();
    }

    @Override
    public String toPrettyString() {
        if (value % 1 == 0) {
            return String.valueOf((int)value);
        }

        return String.valueOf(value);
    }

    @Override
    public Number copy() {
        return new Number(value);
    }

    @Override
    public TermType getTermType() {
        return TermType.NUMBER;
    }

    public double getValue() {
        return value;
    }

    @Override
    public Number evaluate() {
        return this;
    }

    public Number add(Number other) {
        return new Number(this.value + other.value);
    }

    public Number subtract(Number other) {
        return new Number(this.value - other.value);
    }

    public Number multiply(Number other) {
        return new Number(this.value * other.value);
    }

    public Number divide(Number other) {
        return new Number(this.value / other.value);
    }
}
