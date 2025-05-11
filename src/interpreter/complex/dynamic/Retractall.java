package interpreter.complex.dynamic;

import interpreter.Fact;
import interpreter.Substitution;
import interpreter.Term;
import interpreter.FactDatabase;

public class Retractall extends Dynamic {

    public Retractall(Term arg) {
        super("retractall", arg);
    }

    public static boolean isRetractall(String input) {
        return input.matches("^retractall\\(.*\\)");
    }

    @Override
    public Substitution execute(FactDatabase db) {
        if (!db.isDynamic(this.arg)) {
            throw new RuntimeException("Argument is not a dynamic type");
        }

        for (int i = 0; i < db.getFacts().size(); i++) {
            Fact f = db.getFacts().get(i);
            Substitution substitution = f.unify((Term) this.arg);
            if (substitution.isSuccess()) {
                db.getFacts().remove(i);
                i--;
            }
        }

        return Substitution.success();
    }

    @Override
    public Dynamic copy() {
        return new Retractall((Term) this.arg.copy());
    }
}
