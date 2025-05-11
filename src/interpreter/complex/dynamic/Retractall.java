package interpreter.complex.dynamic;

import interpreter.Fact;
import interpreter.Unification;
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
    public Unification execute(FactDatabase db) {
        if (!db.isDynamic(this.arg)) {
            throw new RuntimeException("Argument is not a dynamic type");
        }

        for (int i = 0; i < db.getFacts().size(); i++) {
            Fact f = db.getFacts().get(i);
            Unification unification = f.unify((Term) this.arg);
            if (unification.isSuccess()) {
                db.getFacts().remove(i);
                i--;
            }
        }

        return Unification.success();
    }

    @Override
    public Dynamic copy() {
        return new Retractall((Term) this.arg.copy());
    }
}
