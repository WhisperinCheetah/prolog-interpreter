package interpreter.complex.dynamic;

import interpreter.Fact;
import interpreter.Substitution;
import interpreter.FactDatabase;

public class Assertz extends Dynamic {
    public Assertz(Fact arg) {
        super("assertz", arg);
    }

    public static boolean isAssertz(String input) {
        return input.matches("^assertz\\(.*\\)");
    }

    @Override
    public Substitution execute(FactDatabase db) {
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
