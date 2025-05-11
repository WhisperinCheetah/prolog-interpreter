package interpreter.complex.dynamic;

import interpreter.Fact;
import interpreter.Substitution;
import interpreter.FactDatabase;

public class Asserta extends Dynamic {

    public Asserta(Fact arg) {
        super("asserta", arg);
    }

    public static boolean isAsserta(String input) {
        return input.matches("asserta\\(.*\\)");
    }

    @Override
    public Substitution execute(FactDatabase db) {
        if (!db.isDynamic(this.arg)) {
            throw new RuntimeException("Argument " + this.arg + " is not a dynamic type");
        }

        db.insertFact(this.arg, 0);

        return Substitution.success();
    }

    @Override
    public Dynamic copy() {
        return new Asserta(this.arg.copy());
    }
}
