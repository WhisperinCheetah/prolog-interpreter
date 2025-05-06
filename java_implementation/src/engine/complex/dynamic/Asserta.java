package engine.complex.dynamic;

import engine.Fact;
import engine.Substitution;
import engine.TermDatabase;

public class Asserta extends Dynamic {

    public Asserta(Fact arg) {
        super("asserta", arg);
    }

    public static boolean isAsserta(String input) {
        return input.matches("asserta\\(.*\\)");
    }

    @Override
    public Substitution execute(TermDatabase db) {
        if (!db.isDynamic(this.arg)) {
            throw new RuntimeException("Argument is not a dynamic type");
        }

        db.insertFact(this.arg, 0);

        return Substitution.success();
    }

    @Override
    public Dynamic copy() {
        return new Asserta(this.arg.copy());
    }
}
