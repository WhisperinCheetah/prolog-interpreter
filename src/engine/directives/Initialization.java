package engine.directives;

import engine.complex.ComplexTerm;

public class Initialization extends Directive {
    public Initialization(ComplexTerm goal) {
        super(goal);
    }

    public static boolean isInitialization(String input) {
        return input.matches("^:-initialization\\(.*\\)");
    }
}
