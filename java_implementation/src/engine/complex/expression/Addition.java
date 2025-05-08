package engine.complex.expression;

import engine.simple.Number;

public class Addition extends Expression {

    public Addition(Expression argl, Expression argr) {
        super("+", argl, argr);
    }

    public static boolean isAddition(String input) {
        return Expression.isExpression("+", input);
    }

    @Override
    public Number evaluate() {
        return argl.evaluate().add(argr.evaluate());
    }
}
