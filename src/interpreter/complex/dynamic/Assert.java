package interpreter.complex.dynamic;

import interpreter.Fact;
import interpreter.Unification;
import interpreter.FactDatabase;

public class Assert extends Dynamic {
    public Assert(Fact arg) {
        super("assert", arg);
    }

    public static boolean isAssert(String input) {
        return input.matches("^assert\\(.*\\)");
    }

    @Override
    public Unification execute(FactDatabase db) {
        if (!db.isDynamic(this.arg)) {
            throw new RuntimeException("Argument is not a dynamic type");
        }

        db.addFact(this.arg);

        return Unification.success();
    }

    @Override
    public Dynamic copy() {
        return new Assert(this.arg.copy());
    }
}
