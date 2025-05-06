package engine.directives;

import engine.FunctorType;
import engine.complex.ComplexTerm;

import java.util.List;

public class DynamicDirective extends Directive{
    public DynamicDirective(FunctorType functorType) {
        super(new ComplexTerm(functorType, List.of()));
    }

    public static boolean isDynamic(String input) {
        return input.matches("^:-dynamic\\(.*\\)");
    }
}
