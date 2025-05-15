package interpreter.complex.dynamic;

import interpreter.*;
import interpreter.complex.ComplexTerm;
import interpreter.simple.Variable;

import java.util.HashMap;
import java.util.List;

/**
 * Abstract class Dynamic is similar to predicate but the execute function requires access to the FactDatabase,
 * so this is a different abstract class.
 */
public abstract class Dynamic extends ComplexTerm {

    protected Fact arg;

    public Dynamic(String functor, Fact arg) {
        super(functor, List.of(arg instanceof Rule rule ? rule.getHead() : (Term) arg), 1);
        this.arg = arg;
    }


    /**
     * Executes the dynamic predicate. Adding or retracting a fact from the FactDatabase.
     *
     * @param db the FactDatabase
     * @return a Unification (can fail)
     */
    public abstract Unification execute(FactDatabase db);

    @Override
    public Dynamic renameVariables(HashMap<String, Variable> map) {
        Dynamic copy = this.copy();

        copy.setArgs(copy.getArgs().stream().map(t -> t.renameVariables(map)).toList());
        copy.arg = arg.renameVariables(map);

        return copy;
    }

    @Override
    public Dynamic substituteVariables(Unification unification) {
        Dynamic copy = this.copy();

        copy.setArgs(copy.getArgs().stream().map(t -> t.substituteVariables(unification)).toList());
        copy.arg = arg.substituteVariables(unification);

        return copy;
    }

    @Override
    public abstract Dynamic copy();
}
