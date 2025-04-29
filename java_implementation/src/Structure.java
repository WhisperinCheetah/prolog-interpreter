package src;

import src.simples.Variable;

public abstract class Structure implements Term {
    public abstract Structure copy();
    public void fillVariable(Variable var, Term fill) {}
}
