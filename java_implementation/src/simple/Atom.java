package src.simple;

import src.Substitution;
import src.Term;
import src.TermType;

import java.util.HashMap;

public class Atom extends SimpleTerm {
    private final String value;

    public Atom(String value) {
        this.value = value;
    }

    public static boolean isAtom(String line) {
        return line.matches("[a-z][a-zA-Z]*");
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Atom otherAtom) {
            return value.equals(otherAtom.value);
        }

        return false;
    }

    @Override
    public Substitution unify(Term other) {
        if (other instanceof Variable) return other.unify(this);
        if (other instanceof Atom atom && this.value.equals(atom.value)) return Substitution.success();

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
    public Atom copy() {
        return new Atom(value);
    }

    @Override
    public TermType getTermType() {
        return TermType.ATOM;
    }
}
