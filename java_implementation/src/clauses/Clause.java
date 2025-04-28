package src.clauses;

import src.Structure;
import src.Term;
import src.TermDatabase;
import src.simples.Variable;

public abstract class Clause extends Structure {

    protected final FunctorType functorType;

    public Clause(FunctorType functor) {
        this.functorType = functor;
    }

    public FunctorType getFunctorType() {
        return this.functorType;
    }

    public int getArity() {
        return this.functorType.arity;
    }

    public String getFunctorName() {
        return this.functorType.functor;
    }

    /**
     * Function that fills all variables var with fill
     *
     * @param var: variable to be filled
     * @param fill: term that is used to fill variable
     */
    public abstract void fillVariable(Variable var, Term fill);

    /**
     * Function that takes a database and a query and returns true if the
     * Term and query resolve together.
     *
     * @param db: database
     * @param query: query to resolve
     * @return true if query is resolved with database
     */
    public abstract boolean resolve(TermDatabase db, Fact query);
}
