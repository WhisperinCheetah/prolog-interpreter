package engine.complex.dynamic;

import engine.Fact;
import engine.Substitution;
import engine.TermDatabase;

public class Assert extends Dynamic {
    public Assert(Fact arg) {
        super("assert", arg);
    }

    public static boolean isAssert(String input) {
        return input.matches("^assert\\(.*\\)");
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
        return new Assert(this.arg.copy());
    }
}
