package engine.parser;

import engine.complex.Predicate;
import engine.complex.Write;

import java.util.Optional;

public class PredicateParser {

    public static Optional<Predicate> parse(String input) {
        Optional<Predicate> predicate = WriteParser.parse(input).map(w -> w);

        if (predicate.isEmpty()) {
            predicate = NLParser.parse(input).map(n -> n);
        }

        return predicate;
    }
}
