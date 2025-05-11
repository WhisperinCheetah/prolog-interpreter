package interpreter.complex.expression;

import interpreter.Term;
import interpreter.simple.Number;

public interface EvaluableExpression extends Term {
    Number evaluate();
}
