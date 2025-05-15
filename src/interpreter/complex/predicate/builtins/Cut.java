package interpreter.complex.predicate.builtins;

import interpreter.Unification;
import interpreter.complex.predicate.Predicate;

public class Cut extends Predicate {
    public Cut() {
        super("!");
    }

    public static boolean isCut(String input) {
        return input.matches("^!");
    }

    @Override
    public Unification execute() {
        return Unification.cut();
    }

    @Override
    public Predicate copy() {
        return new Cut();
    }
}
