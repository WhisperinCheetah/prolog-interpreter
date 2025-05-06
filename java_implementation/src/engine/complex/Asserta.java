package engine.complex;

import engine.Fact;
import engine.Substitution;
import engine.Term;
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
        db.insertFact(this.arg, 0);

        return Substitution.success();
    }

    @Override
    public Dynamic copy() {
        return new Asserta(this.arg.copy());
    }
}
