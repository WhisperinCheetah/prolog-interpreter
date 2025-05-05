package engine.parser;

import engine.simple.Number;

import java.util.Optional;

public class NumberParser {

    public static Number parseNumber(String input) {
        return new Number(Double.parseDouble(input));
    }

    public static Optional<Number> parse(String input) {
        if (Number.isNumber(input)) {
            return Optional.of(parseNumber(input));
        }

        return Optional.empty();
    }
}
