package engine.complex.predicate;

import engine.Substitution;
import engine.Term;
import engine.simple.Variable;
import engine.simple.Number;

import java.util.List;

public class Succ extends Predicate {

    private void checkArg(Term arg) {
        if (!(arg instanceof Number || arg instanceof Variable)) throw new IllegalArgumentException("succ argument must be a Variable or a Number not " + arg);
    }

    private void checkArgs(Term argl, Term argr) {
        checkArg(argl);
        checkArg(argr);
    }

    public Succ(List<Term> args) {
        super("succ", args, 2);

        if (args.size() != 2) throw new IllegalArgumentException("succ got too many arguments: " + args.size() + " instead of 2");

        checkArgs(args.getFirst(), args.getLast());
    }

    public Succ(Term argl, Term argr) {
        super("succ", List.of(argl, argr), 2);

        checkArgs(argl, argr);
    }

    @Override
    public Substitution execute() {
        Term argl = args.getFirst();
        Term argr = args.getLast();

        if (argl instanceof Number numl && argr instanceof Number numr) {
            return Substitution.fromBoolean(numr.getValue() - numl.getValue() == 1.0);
        }
        if (argl instanceof Number numl && argr instanceof Variable varr) {
            return Substitution.fromEntry(varr, new Number(numl.getValue() + 1.0));
        }
        if (argl instanceof Variable varl && argr instanceof Number numl) {
            return Substitution.fromEntry(varl, new Number(numl.getValue() - 1.0));
        }

        throw new IllegalArgumentException("Variables " + argl + " and " + argr + " not sufficiently instantiated");
    }

    @Override
    public Succ copy() {
        return new Succ(args.getFirst().copy(), args.getLast().copy());
    }
}
