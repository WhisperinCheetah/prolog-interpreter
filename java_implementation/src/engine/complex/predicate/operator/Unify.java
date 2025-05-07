package engine.complex.predicate.operator;

import engine.Substitution;
import engine.Term;
import engine.complex.predicate.Predicate;

import java.util.List;

public class Unify extends Operator {
    public Unify(Term argl, Term argr) {
        super("=", argl, argr);
    }

    public Unify(List<Term> args) {
        super("=", args);
    }

    public static boolean isUnify(String input) {
        return Operator.isOperator("=", input);
    }


    @Override
    public Substitution execute() {
        return argl().unify(argr());
    }

    @Override
    public Predicate copy() {
        return new Unify(this.getArg(0).copy(), this.getArg(1).copy());
    }
}
