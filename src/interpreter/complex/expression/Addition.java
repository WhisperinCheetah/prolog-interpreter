package interpreter.complex.expression;

import interpreter.Term;
import interpreter.simple.Number;

public class Addition extends BinaryExpression {

    public Addition(Term argl, Term argr) {
        super("+", argl, argr);
    }

    public static boolean isAddition(String input) {
        return BinaryExpression.isExpression("\\+", input);
    }

    @Override
    public Number evaluate() {
        return getSafeArgl().evaluate().add(getSafeArgr().evaluate());
    }

    @Override
    public BinaryExpression copy() {
        return new Addition(argl.copy(), argr.copy());
    }
}
