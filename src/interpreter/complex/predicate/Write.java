package interpreter.complex.predicate;

import interpreter.Unification;
import interpreter.Term;

import java.util.List;

public class Write extends Predicate {

    public Write(Term arg) {
        super("write", List.of(arg), 1);
    }

    public static boolean isWrite(String input) {
        return input.matches("write\\(.*\\)");
    }

    @Override
    public Unification execute() {
        System.out.print(getArgs().getFirst().toPrettyString());

        return Unification.success();
    }

    @Override
    public Write substituteVariables(Unification unification) {
        Term filledArg = getArgs().getFirst().substituteVariables(unification);
        return new Write(filledArg);
    }

    @Override
    public Write copy() {
        return new Write(getArgs().getFirst().copy());
    }
}
