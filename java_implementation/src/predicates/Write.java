package src.predicates;

import src.Structure;
import src.Term;

import java.util.List;

public class Write extends BuiltinPredicate {

    @Override
    boolean execute(List<Term> args) {
        System.out.println(args.getFirst().toString());

        return true;
    }

    @Override
    public Structure copy() {
        return new Write();
    }
}
