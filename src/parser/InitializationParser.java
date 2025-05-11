package parser;

import interpreter.complex.ComplexTerm;
import interpreter.directives.Initialization;

import java.util.Optional;

public class InitializationParser {

    public static Initialization parseInitialization(String input) {
        String goalString = input.substring(input.indexOf("(") + 1, input.lastIndexOf(")"));

        ComplexTerm goal = ComplexTermParser.parseComplexTerm(goalString);

        return new Initialization(goal);
    }

    public static Optional<Initialization> parse(String input) {
        if (Initialization.isInitialization(input)) {
            return Optional.of(parseInitialization(input));
        }

        return Optional.empty();
    }
}
