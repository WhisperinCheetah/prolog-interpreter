package engine.complex.predicate.operator;

import engine.Substitution;
import engine.Term;
import engine.complex.predicate.Predicate;

import java.util.List;

public class NotEquality extends Operator {
    public NotEquality(Term argl, Term argr) {
        super("\\==", argl, argr);
    }

    public NotEquality(List<Term> args) {
        super("\\==", args);
    }

    public static boolean isNotEquality(String input) {
        return Operator.isOperator("\\==", input);
    }

    @Override
    public Substitution execute() {
        return new Substitution(argl().unify(argr()).isFailure());
    }

    @Override
    public Predicate copy() {
        return new NotEquality(argl().copy(), argr().copy());
    }
}
