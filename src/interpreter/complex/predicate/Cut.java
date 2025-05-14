package interpreter.complex.predicate;

import interpreter.FunctorType;
import interpreter.Term;
import interpreter.Unification;

import java.util.List;

public class Cut extends Predicate {
    public Cut() {
        super("!");
    }

    public static boolean isCut(String input) {
        return input.matches("^!");
    }

    @Override
    public Unification execute() {
        return Unification.success();
    }

    @Override
    public Predicate copy() {
        return new Cut();
    }
}
