package engine.complex.expression;

import engine.Term;
import engine.simple.Number;

public class Subtraction extends BinaryExpression {

    public Subtraction(Term argl, Term argr) {
        super("-", argl, argr);
    }

    public static boolean isSubtraction(String input) {
        return BinaryExpression.isExpression("\\-", input);
    }

    @Override
    public Number evaluate() {
        return getSafeArgl().evaluate().subtract(getSafeArgr().evaluate());
    }

    @Override
    public BinaryExpression copy() {
        return new Subtraction(argl.copy(), argr.copy());
    }
}
