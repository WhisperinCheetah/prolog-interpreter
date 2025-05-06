package engine.complex;

import engine.*;
import engine.simple.Variable;

import java.util.HashMap;
import java.util.List;

public abstract class Dynamic extends ComplexTerm {

    protected Fact arg;

    public Dynamic(String functor, Fact arg) {
        super(functor, List.of(arg instanceof Rule rule ? rule.getHead() : (Term) arg), 1);
        this.arg = arg;
    }

    public abstract Substitution execute(TermDatabase db);

    @Override
    public Dynamic renameVariables(HashMap<String, Variable> map) {
        Dynamic copy = (Dynamic) this.copy();

        copy.setArgs(copy.getArgs().stream().map(t -> t.renameVariables(map)).toList());
        copy.arg = arg.renameVariables(map);

        return copy;
    }

    @Override
    public Dynamic substituteVariables(Substitution substitution) {
        Dynamic copy = (Dynamic) this.copy();

        copy.setArgs(copy.getArgs().stream().map(t -> t.substituteVariables(substitution)).toList());
        copy.arg = arg.substituteVariables(substitution);

        return copy;
    }

    @Override
    public abstract Dynamic copy();
}
