package engine;

import engine.simple.Variable;

import java.util.HashMap;

public interface Fact {
    Substitution unify(Term other);
    Fact renameVariables(HashMap<String, Variable> map);

    Fact copy();
    TermType getTermType();
}
