package engine.complex;

import engine.Substitution;

import java.util.List;

public class NL extends Predicate {

    public NL() {
        super("nl", List.of(), 0);
    }

    public static boolean isNL(String input) {
        return input.equals("nl");
    }

    @Override
    public Substitution execute() {
        System.out.println();

        return Substitution.success();
    }

    @Override
    public Predicate substituteVariables(Substitution substitution) {
        return new NL();
    }

    @Override
    public Predicate copy() {
        return new NL();
    }
}
