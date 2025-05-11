package interpreter.complex.predicate;

import interpreter.Substitution;
import interpreter.Term;
import interpreter.complex.expression.EvaluableExpression;
import interpreter.simple.Variable;

import java.util.List;

public class Is extends Predicate {

    private void checkArgs(Term arg1, Term arg2) {
        if (!(arg1 instanceof Variable)) throw new IllegalArgumentException("Left hand argument of is/2 must be evaluable");
        if (!(arg2 instanceof EvaluableExpression)) throw new IllegalArgumentException("Right hand argument of is/2 must be evaluable");
    }

    public Is(List<Term> args) {
        super("is", args, 2);

        if (args.size() != 2) throw new IllegalArgumentException("Too many arguments provided to is/2");

        checkArgs(args.get(0), args.get(1));
    }

    public Is(Term arg1, Term arg2) {
        super("is", List.of(arg1, arg2), 2);

        checkArgs(arg1, arg2);
    }

    public static boolean isIs(String input) {
        return input.matches("^is\\(.*,.*\\)");
    }

    @Override
    public Substitution execute() {
        Variable var = (Variable) getArg(0);
        EvaluableExpression evaluable = (EvaluableExpression) getArg(1);

        return Substitution.fromEntry(var, evaluable.evaluate());
    }

    @Override
    public Predicate copy() {
        return new Is(getArg(0).copy(), getArg(1).copy());
    }
}
