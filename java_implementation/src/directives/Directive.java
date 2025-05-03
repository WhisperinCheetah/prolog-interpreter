package src.directives;

import src.Structure;

import java.util.Optional;

// TODO extends structure?
public abstract class Directive {
    protected Structure goal;

    public Directive(Structure goal) {
        this.goal = goal;
    }

    public Structure getGoal() {
        return goal;
    }

    public void setGoal(Structure goal) {
        this.goal = goal;
    }

    public static boolean isDirective(String line) {
        return line.startsWith(":-");
    }
}
