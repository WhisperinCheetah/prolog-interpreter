package engine.complex.expression;

import engine.Substitution;
import engine.Term;
import engine.TermType;
import engine.simple.Number;
import engine.simple.Variable;

import java.util.HashMap;

public abstract class BinaryExpression implements Term, EvaluableExpression {

    protected String functor;
    protected Term argl;
    protected Term argr;

    public BinaryExpression(String functor, Term argl, Term argr) {
        this.functor = functor;
        this.argl = argl;
        this.argr = argr;
    }

    public static boolean isExpression(String op, String input) {
        return input.matches("^" + op + "\\(.*,.*\\)");
    }

    public void evaluateCheck() {
        if (!(this.argl instanceof EvaluableExpression)) throw new RuntimeException("Left argument not properly instantiated (" + this.argl + ")");
        if (!(this.argr instanceof EvaluableExpression)) throw new RuntimeException("Left argument not properly instantiated (" + this.argr + ")");
    }

    public abstract Number evaluate();

    @Override
    public BinaryExpression renameVariables(HashMap<String, Variable> map) {
        BinaryExpression copy = this.copy();

        copy.argl = this.argl.renameVariables(map);
        copy.argr = this.argr.renameVariables(map);

        return copy;
    }

    @Override
    public BinaryExpression substituteVariables(Substitution substitution) {
        BinaryExpression copy = this.copy();

        copy.argl = this.argl.substituteVariables(substitution);
        copy.argr = this.argr.substituteVariables(substitution);

        return copy;
    }

    @Override
    public Substitution unify(Term other) {
        throw new UnsupportedOperationException("Expressions cannot be unified.");
    }

    @Override
    public abstract BinaryExpression copy();

    @Override
    public TermType getTermType() {
        return null;
    }

    @Override
    public String toString() {
        return functor + "(" + argl.toString() + "," + argr.toString() + ")";
    }

    protected EvaluableExpression getSafeArgl() {
        if (!(this.argl instanceof EvaluableExpression)) throw new RuntimeException("Left argument not properly instantiated (" + this.argl + ")");

        return (EvaluableExpression) this.argl;
    }

    protected EvaluableExpression getSafeArgr() {
        if (!(this.argr instanceof EvaluableExpression)) throw new RuntimeException("Right argument not properly instantiated (" + this.argr + ")");

        return (EvaluableExpression) this.argr;
    }
}
