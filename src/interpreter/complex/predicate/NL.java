package interpreter.complex.predicate;

import interpreter.Unification;

import java.util.List;

public class NL extends Predicate {

    public NL() {
        super("nl", List.of(), 0);
    }

    public static boolean isNL(String input) {
        return input.equals("nl");
    }

    @Override
    public Unification execute() {
        System.out.println();

        return Unification.success();
    }

    @Override
    public Predicate substituteVariables(Unification unification) {
        return new NL();
    }

    @Override
    public Predicate copy() {
        return new NL();
    }
}
