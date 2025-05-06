package engine.parser;

import engine.FunctorType;
import engine.Term;
import engine.directives.DynamicDirective;

import java.util.Optional;

public class DynamicDirectiveParser {

    public static DynamicDirective parseDynamicDirective(String input) {
        String argString = input.substring(input.indexOf("(") + 1, input.lastIndexOf(")"));

        String functor = argString.split("/")[0];
        int arity = Integer.parseInt(argString.split("/")[1]);

        FunctorType type = new FunctorType(functor, arity);

        return new DynamicDirective(type);
    }

    public static Optional<DynamicDirective> parse(String input) {
        if (DynamicDirective.isDynamic(input)) {
            return Optional.of(parseDynamicDirective(input));
        }

        return Optional.empty();
    }
}
