package engine.simple;

import engine.Substitution;
import engine.Term;
import engine.TermType;
import engine.complex.expression.EvaluableExpression;

import java.util.HashMap;

public class Variable extends SimpleTerm implements EvaluableExpression {
    String name;

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static boolean isVariable(String line) {
        return line.matches("([A-Z]|_)[a-zA-Z]*");
    }

    @Override
    public String toString() {
        // return this.name + "[" + super.toString().split("@")[1] + "]";
        return name;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public Substitution unify(Term other) {
        return Substitution.fromEntry(this, other);
    }

    @Override
    public Term renameVariables(HashMap<String, Variable> map) {
        if (!map.containsKey(this.name)) map.put(this.name, this.copy());

        return map.get(this.name);
    }

    @Override
    public Term substituteVariables(Substitution substitution) {
        return substitution.getMap().getOrDefault(this, this);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public Variable copy() {
        return new Variable(this.name);
    }

    @Override
    public TermType getTermType() {
        return TermType.VAR;
    }

    public Variable get() {
        return this;
    }

    @Override
    public Number evaluate() {
        throw new RuntimeException("Variable " + this.name + " not instantiated when executing expression");
    }
}
