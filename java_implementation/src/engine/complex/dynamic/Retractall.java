package engine.complex.dynamic;

import engine.Fact;
import engine.Substitution;
import engine.Term;
import engine.TermDatabase;

public class Retractall extends Dynamic {

    public Retractall(Term arg) {
        super("retractall", arg);
    }

    public static boolean isRetractall(String input) {
        return input.matches("^retractall\\(.*\\)");
    }

    @Override
    public Substitution execute(TermDatabase db) {
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
