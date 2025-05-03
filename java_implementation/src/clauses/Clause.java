package src.clauses;

import src.Structure;
import src.Term;
import src.TermDatabase;
import src.simples.Variable;

public abstract class Clause extends Structure {

    public Clause(FunctorType functor) {
        super(functor);
    }

    public abstract boolean equalsIgnoreVars(Fact other);
}
