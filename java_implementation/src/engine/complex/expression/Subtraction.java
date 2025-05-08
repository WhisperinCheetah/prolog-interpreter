package engine.complex.expression;

import engine.simple.Number;

public class Subtraction extends Expression {

    public Subtraction(Expression argl, Expression argr) {
        super("-", argl, argr);
    }

    public static boolean isSubtraction(String input) {
        return Expression.isExpression("-", input);
    }

    @Override
    public Number evaluate() {
        return argl.evaluate().subtract(argl.evaluate());
    }
}
