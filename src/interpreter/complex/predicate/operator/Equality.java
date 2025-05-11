package interpreter.complex.predicate.operator;

import interpreter.Unification;
import interpreter.Term;
import interpreter.complex.predicate.Predicate;

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
    public Unification execute() {
        return new Unification(argl().unify(argr()).isSuccess());
    }

    @Override
    public Predicate copy() {
        return new Equality(argl().copy(), argr().copy());
    }
}
