package interpreter.complex.predicate.operator;

import interpreter.Unification;
import interpreter.Term;
import interpreter.complex.predicate.Predicate;

import java.util.List;

public class NotUnify extends Operator {
    public NotUnify(Term argl, Term argr) {
        super("\\=", argl, argr);
    }

    public NotUnify(List<Term> args) {
        super("\\=", args);
    }

    public static boolean isNotUnify(String input) {
        System.out.println("input is not unify? " + Operator.isOperator("\\=", input));
        return Operator.isOperator("\\=", input);
    }

    @Override
    public Unification execute() {
        return new Unification(argl().unify(argr()).isFailure());
    }

    @Override
    public Predicate copy() {
        return new NotUnify(argl().copy(), argr().copy());
    }
}
