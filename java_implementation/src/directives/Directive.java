package src.directives;

import src.Structure;

import java.util.Optional;

public abstract class Directive {
    protected final Structure goal;

    public Directive(Structure goal) {
        this.goal = goal;
    }

    public Structure getGoal() {
        return goal;
    }

    public static boolean isDirective(String line) {
        return line.startsWith(":-");
    }
}
