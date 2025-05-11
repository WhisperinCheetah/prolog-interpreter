package interpreter;

import interpreter.simple.Variable;

import java.util.HashMap;

public interface Term extends Fact {

    @Override
    Term renameVariables(HashMap<String, Variable> map);

    @Override
    Term substituteVariables(Substitution substitution);

    String toPrettyString();

    Term copy();
}
