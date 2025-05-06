package engine.complex;

import java.util.*;

import engine.*;
import engine.simple.SimpleTerm;
import engine.simple.Variable;

public class ComplexTerm implements Term {

    FunctorType type;
    private List<Term> args;

    public ComplexTerm(String functor, List<Term> args, int arity) {
        this.type = new FunctorType(functor, arity);
        this.args = args;
    }

    public ComplexTerm(String functor, List<Term> args) {
        this.type = new FunctorType(functor, args.size());
        this.args = args;
    }

    public ComplexTerm(FunctorType functor, List<Term> args) {
        this.type = functor;
        this.args = args;
    }

    public ComplexTerm(String functor) {
        this.type = new FunctorType(functor, 0);
        this.args = new ArrayList<>();
    }

    public ComplexTerm(ComplexTerm other) {
        this.type = other.type;
        this.args = other.args.stream().map(Term::copy).toList();
    }

    public ComplexTerm() {
        this.type = new FunctorType("", 0);
        this.args = new ArrayList<>();
    }

    public FunctorType getType() {
        return type;
    }

    public int getArity() {
        return this.type.getArity();
    }

    public String getFunctor() {
        return this.type.getFunctor();
    }

    public static boolean isComplexTerm(String line) {
        return line.matches("[a-z]+[a-zA-Z]*(\\([^:-]*\\))?");
    }

    public Term getArg(int i) {
        return args.get(i);
    }

    public List<Term> getArgs() {
        return args;
    }

    public void setArgs(List<Term> args) {
        this.args = args;
    }

    @Override
    public Substitution unify(Term other) {
        if (other instanceof Variable) return other.unify(this);
        if (other instanceof SimpleTerm) return Substitution.failure();

        ComplexTerm otherComplex = (ComplexTerm) other;

        if (!this.type.equals(otherComplex.getType())) return Substitution.failure();

        Substitution substitution = Substitution.success();
        for (int i = 0; i < getArity(); i++) {
            Term argl = this.getArg(i).substituteVariables(substitution);
            Term argr = otherComplex.getArg(i).substituteVariables(substitution);
            Substitution argSubstitution = argl.unify(argr);
            substitution = substitution.unify(argSubstitution);
        }

        return substitution;
    }

    @Override
    public ComplexTerm renameVariables(HashMap<String, Variable> map) {
        ComplexTerm copy = this.copy();

        copy.args = copy.args.stream().map(t -> t.renameVariables(map)).toList();

        return copy;
    }

    @Override
    public ComplexTerm substituteVariables(Substitution substitution) {
        List<Term> filledArgs = this.args.stream().map(t -> t.substituteVariables(substitution)).toList();
        return new ComplexTerm(this.type, filledArgs);
    }

    @Override
    public ComplexTerm copy() {
        return new ComplexTerm(this);
    }

    @Override
    public TermType getTermType() {
        return TermType.COMPLEX;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)/%d", type.getFunctor(), args, getArity());
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ComplexTerm otherComplexTerm) {
            if (!this.getType().equals(otherComplexTerm.getType())) {
                return false;
            }

            for (int i = 0; i < this.getArity(); i++) {
                if (!otherComplexTerm.args.get(i).equals(this.args.get(i))) {
                    return false;
                }
            }

            return true;

        }

        return false;
    }

    public boolean equalsIgnoreVars(ComplexTerm other) {
        if (!this.getType().equals(other.getType())) {
            return false;
        }

        for (int i = 0; i < this.getArity(); i++) {
            if (! (other.args.get(i) instanceof Variable || this.args.get(i) instanceof Variable || this.args.get(i).equals(other.args.get(i))) ) {
                return false;
            }
        }

        return true;
    }

    public boolean shallowEquals(ComplexTerm other) {
        return this.getType().equals(other.getType());
    }

}
