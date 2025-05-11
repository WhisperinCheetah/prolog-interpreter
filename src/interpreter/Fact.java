package interpreter;

import interpreter.simple.Variable;

import java.util.HashMap;

public interface Fact {
    Substitution unify(Term other);
    Fact renameVariables(HashMap<String, Variable> map);
    Fact substituteVariables(Substitution substitution);

    Fact copy();
}
