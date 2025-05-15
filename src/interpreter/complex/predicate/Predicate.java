package interpreter.complex.predicate;

import interpreter.FunctorType;
import interpreter.Unification;
import interpreter.Term;
import interpreter.complex.ComplexTerm;
import interpreter.simple.Variable;

import java.util.HashMap;
import java.util.List;

public abstract class Predicate extends ComplexTerm {

    public Predicate(String functor, List<Term> args, int arity) {
        super(functor, args, arity);
    }

    public Predicate(FunctorType functor, List<Term> args) {
        super(functor, args);
    }

    public Predicate(String s) {
        super(s);
    }

    /**
     * Predicates usually need to execute something (like a Write or a Fail). This gets called when trying to backtrack
     * any Predicate.
     *
     * @return a Unification
     */
    public abstract Unification execute();

    @Override
    public Predicate renameVariables(HashMap<String, Variable> map) {
        Predicate copy = this.copy();

        copy.setArgs(this.getArgs().stream().map(t -> t.renameVariables(map)).toList());

        return copy;
    }

    @Override
    public Predicate substituteVariables(Unification unification) {
        Predicate copy = this.copy();

        copy.setArgs(this.getArgs().stream().map(t -> t.substituteVariables(unification)).toList());

        return copy;
    }

    @Override
    public abstract Predicate copy();
}
