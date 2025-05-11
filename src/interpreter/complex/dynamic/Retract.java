package interpreter.complex.dynamic;

import interpreter.Fact;
import interpreter.Unification;
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
    public Unification execute(FactDatabase db) {
        if (!db.isDynamic(this.arg)) {
            throw new RuntimeException("Argument is not a dynamic type");
        }

        for (int i = 0; i < db.getFacts().size(); i++) {
            Fact f = db.getFacts().get(i);
            Unification unification = f.unify((Term) this.arg);
            if (unification.isSuccess()) {
                db.getFacts().remove(i);
                return unification;
            }
        }

        return Unification.failure();
    }

    @Override
    public Dynamic copy() {
        return new Retract((Term)this.arg.copy());
    }
}
