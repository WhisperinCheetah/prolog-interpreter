package src.predicates;

import src.Term;
import src.TermType;

import java.util.List;

public class Write extends BuiltinPredicate {

    @Override
    boolean execute(List<Term> args) {
        System.out.println(args.getFirst().toString());

        return true;
    }

    public Write copy() {
        return new Write();
    }
}
