package engine.complex.dynamic;

import engine.Fact;
import engine.Substitution;
import engine.Term;
import engine.TermDatabase;

public class Retract extends Dynamic {
    public Retract(Term arg) {
        super("retract", arg);
    }

    public static boolean isRetract(String input) {
        return input.matches("^retract\\(.*\\)");
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
                return substitution;
            }
        }

        return Substitution.failure();
    }

    @Override
    public Dynamic copy() {
        return new Retract((Term)this.arg.copy());
    }
}
