package engine.complex.expression;

import engine.Term;
import engine.simple.Number;

public interface EvaluableExpression extends Term {
    Number evaluate();
}
