package src.predicates;

import src.Structure;
import src.TermDatabase;
import src.clauses.FunctorType;

public class NL extends BuiltinPredicate {

    public NL() {
        super(new FunctorType("nl", 0));
    }

    public static boolean isNL(String line) {
        return line.matches("nl");
    }

    public static NL fromString(String line) {
        return new NL();
    }

    @Override
    public boolean execute(TermDatabase db) {
        System.out.println();

        return true;
    }

    @Override
    public Structure copy() {
        return null;
    }

    @Override
    public String toString() {
        return "nl";
    }
}
