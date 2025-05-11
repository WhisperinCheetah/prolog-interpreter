package engine.complex.predicate;

import engine.FunctorType;
import engine.Substitution;
import engine.Term;
import engine.complex.ComplexTerm;
import engine.complex.dynamic.Dynamic;
import engine.simple.Variable;

import java.util.HashMap;
import java.util.List;

public abstract class Predicate extends ComplexTerm {

    public Predicate(String functor, List<Term> args, int arity) {
        super(functor, args, arity);
    }

    public Predicate(FunctorType functor, List<Term> args) {
        super(functor, args);
    }

    public abstract Substitution execute();

    @Override
    public Predicate renameVariables(HashMap<String, Variable> map) {
        Predicate copy = (Predicate) this.copy();

        copy.setArgs(this.getArgs().stream().map(t -> t.renameVariables(map)).toList());

        return copy;
    }

    @Override
    public Predicate substituteVariables(Substitution substitution) {
        Predicate copy = this.copy();

        copy.setArgs(this.getArgs().stream().map(t -> t.substituteVariables(substitution)).toList());

        return copy;
    }

    @Override
    public abstract Predicate copy();
}
