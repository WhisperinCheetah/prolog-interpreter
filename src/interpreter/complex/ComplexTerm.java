package interpreter.complex;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import interpreter.*;
import interpreter.simple.Atom;
import interpreter.simple.SimpleTerm;
import interpreter.simple.Variable;

public class ComplexTerm implements Term {

    public static final String COMPLEX_TERM_REGEX = "[a-z]+[a-zA-Z_]*(\\([^:-]*\\))?";

    FunctorType type;
    protected List<Term> args;

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
        return line.matches(COMPLEX_TERM_REGEX);
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

    public List<Variable> getVariables() {
        List<Variable> vars = new ArrayList<>();
        for (Term arg : this.args) {
            if (arg instanceof Variable var) {
                vars.add(var);
            }
        }

        return vars;
    }

    @Override
    public Unification unify(Term other) {
        if (other instanceof Variable) return other.unify(this);
        if (other instanceof Atom atom) return Unification.fromBoolean(this.getArity() == 0 && this.getFunctor().equals(atom.getValue()));
        if (other instanceof SimpleTerm) return Unification.failure();

        ComplexTerm otherComplex = (ComplexTerm) other;

        if (!this.type.equals(otherComplex.getType())) return Unification.failure();

        return IntStream.range(0, getArity())
                .mapToObj(i -> {
                    Term left = getArg(i).substituteVariables(Unification.success());
                    Term right = otherComplex.getArg(i).substituteVariables(Unification.success());
                    return left.unify(right);
                })
                .reduce(Unification.success(), Unification::unify);
    }

    @Override
    public ComplexTerm renameVariables(HashMap<String, Variable> map) {
        ComplexTerm copy = this.copy();

        copy.args = copy.args.stream().map(t -> t.renameVariables(map)).toList();

        return copy;
    }

    @Override
    public ComplexTerm substituteVariables(Unification unification) {
        List<Term> filledArgs = this.args.stream().map(t -> t.substituteVariables(unification)).toList();
        return new ComplexTerm(this.type, filledArgs);
    }

    @Override
    public String toPrettyString() {
        if (this.getArity() == 0) return this.getFunctor();

        return this.getFunctor() + "(" + args.stream().map(Term::toPrettyString).collect(Collectors.joining(", ")) + ")";
    }

    @Override
    public ComplexTerm copy() {
        return new ComplexTerm(this);
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
}
