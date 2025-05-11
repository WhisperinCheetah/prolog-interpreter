package interpreter.complex.predicate;

import interpreter.Unification;

import java.util.List;

public class Fail extends Predicate {

    public Fail() {
        super("fail", List.of(), 0);
    }

    public static boolean isFail(String input) {
        return input.equals("fail");
    }

    @Override
    public Unification execute() {
        return Unification.failure();
    }

    @Override
    public Fail substituteVariables(Unification unification) {
        return this.copy();
    }

    @Override
    public Fail copy() {
        return new Fail();
    }
}
