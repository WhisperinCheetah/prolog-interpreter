package src.simple;

import src.Substitution;
import src.Term;
import src.TermType;

public class Variable extends SimpleTerm {
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
        return this.name;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Variable otherVar) {
            return this.name.equals(otherVar.getName());
        }

        return false;
    }

    @Override
    public Substitution unify(Term other) {
        return Substitution.ofEntry(this, other);
    }

    @Override
    public Term substituteVariables(Substitution substitution) {
        return substitution.getMap().getOrDefault(this, this.copy());
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public Variable copy() {
        return new Variable(this.name);
    }

    @Override
    public TermType getTermType() {
        return TermType.VAR;
    }
}
