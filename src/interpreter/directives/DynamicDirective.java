package interpreter.directives;

import interpreter.FunctorType;
import interpreter.complex.ComplexTerm;

import java.util.List;

public class DynamicDirective extends Directive{
    public DynamicDirective(FunctorType functorType) {
        super(new ComplexTerm(functorType, List.of()));
    }

    public static boolean isDynamic(String input) {
        return input.matches("^:-dynamic\\(.*\\)");
    }

    @Override
    public String toPrettyString() {
        return ":- dynamic " + goal.getType();
    }
}
