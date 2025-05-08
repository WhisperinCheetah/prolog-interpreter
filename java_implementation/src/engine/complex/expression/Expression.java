package engine.complex.expression;

import engine.simple.Number;

public abstract class Expression {

    protected String functor;
    protected Expression argl;
    protected Expression argr;

    public Expression(String functor, Expression argl, Expression argr) {
        this.functor = functor;
        this.argl = argl;
        this.argr = argr;
    }

    public static boolean isExpression(String op, String input) {
        return input.matches("^" + op + "\\(.*,.*\\)");
    }

    public abstract Number evaluate();
}
