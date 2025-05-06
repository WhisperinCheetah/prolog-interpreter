package engine.complex.predicate;

import engine.Substitution;
import engine.Term;

import java.util.List;

public class Write extends Predicate {

    public Write(Term arg) {
        super("write", List.of(arg), 1);
    }

    public static boolean isWrite(String input) {
        return input.matches("write\\(.*\\)");
    }

    @Override
    public Substitution execute() {
        System.out.print(getArgs().getFirst());

        return Substitution.success();
    }

    @Override
    public Write substituteVariables(Substitution substitution) {
        Term filledArg = getArgs().getFirst().substituteVariables(substitution);
        return new Write(filledArg);
    }

    @Override
    public Write copy() {
        return new Write(getArgs().getFirst().copy());
    }
}
