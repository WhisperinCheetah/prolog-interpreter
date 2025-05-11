package interpreter.complex.predicate;

import interpreter.Unification;
import interpreter.Term;
import interpreter.simple.Number;
import interpreter.simple.Variable;

import java.util.List;

public class Between extends Predicate {
    public Between(List<Term> args) {
        super("between", args, 3);

        if (args.size() != 3) {
            throw new IllegalArgumentException("between/3 expects exactly 3 arguments but " + args.size() + " were given.");
        }
    }

    public static boolean isBetween(String input) {
        return input.matches("^between\\(.*,.*,.*\\)");
    }

    @Override
    public Unification execute() {
        Term low = args.getFirst();
        Term high = args.get(1);
        Term value = args.getLast();

        if (low instanceof Number lownum && high instanceof Number highnum) {
            if (value instanceof Number valnum) {
                return Unification.fromBoolean(lownum.getValue() <= valnum.getValue() && valnum.getValue() <= highnum.getValue());
            } else if (value instanceof Variable valvar) {
                return Unification.fromEntry(valvar, lownum);
            }
        }

        throw new IllegalArgumentException("Low and high must be numbers, value must be a number or a variable");
    }

    @Override
    public Predicate copy() {
        return new Between(args.stream().map(Term::copy).toList());
    }
}
