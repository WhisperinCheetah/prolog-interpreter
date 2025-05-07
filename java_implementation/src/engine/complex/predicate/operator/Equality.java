package engine.complex.predicate.operator;

import engine.Substitution;
import engine.Term;
import engine.complex.predicate.Predicate;

import java.util.List;

public class Equality extends Operator {

    public Equality(Term argl, Term argr) {
        super("==", argl, argr);
    }

    public Equality(List<Term> args) {
        super("==", args);
    }

    public static boolean isEquality(String input) {
        return Operator.isOperator("==", input);
    }

    @Override
    public Substitution execute() {
        return new Substitution(argl().unify(argr()).isSuccess());
    }

    @Override
    public Predicate copy() {
        return new Equality(argl().copy(), argr().copy());
    }
}
