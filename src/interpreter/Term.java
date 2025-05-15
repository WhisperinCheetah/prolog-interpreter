package interpreter;

import interpreter.simple.Variable;

import java.util.HashMap;

public interface Term extends Fact {

    @Override
    Term renameVariables(HashMap<String, Variable> map);

    @Override
    Term substituteVariables(Unification unification);


    /**
     * @return a prettified toString of the term
     */
    String toPrettyString();

    @Override
    Term copy();
}
