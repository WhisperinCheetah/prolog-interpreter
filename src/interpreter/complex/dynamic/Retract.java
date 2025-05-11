package interpreter.complex.dynamic;

import interpreter.Fact;
import interpreter.Substitution;
import interpreter.Term;
import interpreter.FactDatabase;

public class Retract extends Dynamic {
    public Retract(Term arg) {
        super("retract", arg);
    }

    public static boolean isRetract(String input) {
        return input.matches("^retract\\(.*\\)");
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
