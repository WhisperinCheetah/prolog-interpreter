package interpreter.complex.predicate.operator;

import interpreter.Term;
import interpreter.complex.predicate.Predicate;

import java.util.List;

public abstract class Operator extends Predicate {
    public Operator(String functor, Term argl, Term argr) {
        super(functor, List.of(argl, argr), 2);
    }

    protected static boolean isOperator(String op, String input) {
        return input.matches("^" + op + "\\(.*,.*\\)");
    }

    public Operator(String functor, List<Term> args) {
        super(functor, args, 2);

        if (args.size() != 2) {
            throw new IllegalArgumentException("Unify requires two arguments");
        }
    }

    protected Term argl() {
        return getArg(0);
    }

    protected Term argr() {
        return getArg(1);
    }
}
