package engine.complex.expression;

import engine.simple.Number;

public class Multiplication extends Expression {

    public Multiplication(Expression argl, Expression argr) {
        super("*", argl, argr);
    }

    public static boolean isMultiplication(String input) {
        return Expression.isExpression("*", input);
    }

    @Override
    public Number evaluate() {
        return argl.evaluate().multiply(argl.evaluate());
    }
}
