package src;

import src.simple.Variable;

import java.util.HashMap;

public interface Term extends Fact {
    Term renameVariables(HashMap<String, Variable> map);
    Term substituteVariables(Substitution substitution);
    Term copy();
}
