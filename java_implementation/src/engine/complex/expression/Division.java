package engine.complex.expression;

import engine.simple.Number;

public class Division extends Expression {

    public Division(Expression argl, Expression argr) {
        super("/", argl, argr);
    }

    public static boolean isDivision(String input) {
        return Expression.isExpression("/", input);
    }

    @Override
    public Number evaluate() {
        return argl.evaluate().divide(argr.evaluate());
    }
}
