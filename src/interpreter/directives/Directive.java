package interpreter.directives;

import interpreter.complex.ComplexTerm;

public abstract class Directive {
    protected final ComplexTerm goal;

    public Directive(ComplexTerm goal) {
        this.goal = goal;
    }

    public ComplexTerm getGoal() {
        return goal;
    }

    public static boolean isDirective(String line) {
        return line.startsWith(":-");
    }
}
