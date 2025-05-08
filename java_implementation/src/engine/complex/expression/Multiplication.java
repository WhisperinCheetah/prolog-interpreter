package engine.complex.expression;

import engine.Term;
import engine.simple.Number;

public class Multiplication extends BinaryExpression {

    public Multiplication(Term argl, Term argr) {
        super("*", argl, argr);
    }

    public static boolean isMultiplication(String input) {
        return BinaryExpression.isExpression("\\*", input);
    }

    @Override
    public Number evaluate() {
        return getSafeArgl().evaluate().multiply(getSafeArgr().evaluate());
    }

    @Override
    public BinaryExpression copy() {
        return new Multiplication(argl.copy(), argr.copy());
    }
}
