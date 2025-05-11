package engine.complex.predicate;

import engine.Substitution;

import java.util.List;

public class Fail extends Predicate {

    public Fail() {
        super("fail", List.of(), 0);
    }

    public static boolean isFail(String input) {
        return input.equals("fail");
    }

    @Override
    public Substitution execute() {
        return Substitution.failure();
    }

    @Override
    public Fail substituteVariables(Substitution substitution) {
        return this.copy();
    }

    @Override
    public Fail copy() {
        return new Fail();
    }
}
