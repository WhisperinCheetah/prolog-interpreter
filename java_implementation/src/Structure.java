package src;

import src.clauses.FunctorType;
import src.simples.Variable;

public abstract class Structure implements Term {

    protected final FunctorType functorType;

    public Structure(FunctorType functor) {
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
     * Executes a structure in relation to a database
     *
     * @param db the database to execute the structure in
     * @return true on success
     */
    public abstract boolean execute(TermDatabase db);

    /**
     * Fills all Variables var with a Term fill
     *
     * @param var the variable to be filled
     * @param fill the fill to put in place of the variable
     */
    public void fillVariable(Variable var, Term fill) {}

    public abstract Structure copy();
}
