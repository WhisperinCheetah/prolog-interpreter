package interpreter;

import interpreter.simple.Variable;

import java.util.HashMap;

public interface Fact {
    /**
     * Tries to unify a fact with a Term. Returns a Unification object.
     *
     * @param other The Term to get unified with
     * @return a Unification object (success and variable map)
     */
    Unification unify(Term other);

    /**
     * Renames variables in a fact so that Variables with the same name get 'linked'.
     *
     * @param map The variable name to variable object map
     * @return a copy of the fact with renamed variables
     */
    Fact renameVariables(HashMap<String, Variable> map);

    /**
     * Substitutes variables that are in the variable map of the Unification object.
     *
     * @param unification The unification with a variable mapping
     * @return a copy of the fact with substituted variables
     */
    Fact substituteVariables(Unification unification);


    /**
     * Creates a deep copy of a fact
     *
     * @return a copy of the fact
     */
    Fact copy();
}
