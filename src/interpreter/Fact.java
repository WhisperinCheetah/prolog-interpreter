package interpreter;

import interpreter.simple.Variable;

import java.util.HashMap;

public interface Fact {
    Unification unify(Term other);
    Fact renameVariables(HashMap<String, Variable> map);
    Fact substituteVariables(Unification unification);

    Fact copy();
}
