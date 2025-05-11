package engine.simple;

import java.util.HashMap;

import engine.Substitution;
import engine.Term;
import engine.TermType;
import engine.complex.ComplexTerm;
import engine.simple.SimpleTerm;

public class Atom extends SimpleTerm {
    private final String value;

    public Atom(String value) {
        this.value = value;
    }

    public static boolean isAtom(String line) {
        return line.matches("[a-z][a-zA-Z]*") || line.matches("\"[^\"]*\"") || line.matches("'[^']*'");
    }

    public String getValue() {
        return value;
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
        if (other instanceof ComplexTerm) return other.unify(this);

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
        return value;
    }

    @Override
    public Atom copy() {
        return new Atom(value);
    }

    @Override
    public
    TermType getTermType() {
        return TermType.ATOM;
    }
}
