package interpreter.complex.expression;

import interpreter.Term;
import interpreter.simple.Number;

/**
 * Interface that makes a class evaluable by adding a function evaluate. Gets used in expressions.
 */
public interface EvaluableExpression extends Term {

    /**
     * Evaluates an expression, returns a Number or throws an error if a Variable is not yet instantiated
     *
     * @return a Number, a value
     */
    Number evaluate();
}
