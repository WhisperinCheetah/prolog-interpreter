package interpreter.complex.expression;

import interpreter.Term;
import interpreter.simple.Number;

public class Division extends BinaryExpression {

    public Division(Term argl, Term argr) {
        super("/", argl, argr);
    }

    public static boolean isDivision(String input) {
        return BinaryExpression.isExpression("\\/", input);
    }

    @Override
    public Number evaluate() {
        return getSafeArgl().evaluate().divide(getSafeArgr().evaluate());
    }

    @Override
    public BinaryExpression copy() {
        return new Division(argl.copy(), argr.copy());
    }
}
