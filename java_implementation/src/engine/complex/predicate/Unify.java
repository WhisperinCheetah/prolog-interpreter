package engine.complex.predicate;

import engine.Substitution;
import engine.Term;

import java.util.List;

public class Unify extends Predicate {
    public Unify(Term argl, Term argr) {
        super("=", List.of(argl, argr), 2);
    }

    public Unify(List<Term> args) {
        super("=", args, 2);

        if (args.size() != 2) {
            throw new IllegalArgumentException("Unify requires two arguments");
        }
    }

    public static boolean isUnify(String input) {
        return input.matches("^=\\(.*,.*\\)");
    }

    private Term argl() {
        return getArg(0);
    }

    private Term argr() {
        return getArg(1);
    }

    @Override
    public Substitution execute() {
        return argl().unify(argr());
    }

    @Override
    public Predicate copy() {
        return new Unify(this.getArg(0), this.getArg(1));
    }
}
