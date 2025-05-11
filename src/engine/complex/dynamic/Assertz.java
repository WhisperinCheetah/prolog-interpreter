package engine.complex.dynamic;

import engine.Fact;
import engine.Substitution;
import engine.TermDatabase;

public class Assertz extends Dynamic {
    public Assertz(Fact arg) {
        super("assertz", arg);
    }

    public static boolean isAssertz(String input) {
        return input.matches("^assertz\\(.*\\)");
    }

    @Override
    public Substitution execute(TermDatabase db) {
        if (!db.isDynamic(this.arg)) {
            throw new RuntimeException("Argument is not a dynamic type");
        }

        db.addFact(this.arg);

        return Substitution.success();
    }

    @Override
    public Dynamic copy() {
        return new Assertz(this.arg.copy());
    }
}
