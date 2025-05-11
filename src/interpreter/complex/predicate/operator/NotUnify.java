package interpreter.complex.predicate.operator;

import interpreter.Substitution;
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
        return Operator.isOperator("\\=", input);
    }

    @Override
    public Substitution execute() {
        return new Substitution(argl().unify(argr()).isFailure());
    }

    @Override
    public Predicate copy() {
        return new NotUnify(argl().copy(), argr().copy());
    }
}
