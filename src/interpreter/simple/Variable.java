package interpreter.simple;

import interpreter.Unification;
import interpreter.Term;
import interpreter.complex.expression.EvaluableExpression;

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
    public Unification unify(Term other) {
        return Unification.fromEntry(this, other);
    }

    @Override
    public Term renameVariables(HashMap<String, Variable> map) {
        if (this.name.equals("_")) return this;

        if (!map.containsKey(this.name)) map.put(this.name, this.copy());

        return map.get(this.name);
    }

    @Override
    public Term substituteVariables(Unification unification) {
        Term newTerm = unification.getMap().getOrDefault(this, this);

        if (newTerm instanceof Variable var && var.equals(this)) {
            return this;
        }

        return newTerm.substituteVariables(unification);
    }

    @Override
    public String toPrettyString() {
        return name;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public Variable copy() {
        return new Variable(this.name);
    }

    public Variable get() {
        return this;
    }

    @Override
    public Number evaluate() {
        throw new RuntimeException("Variable " + this.name + " not instantiated when executing expression");
    }
}
