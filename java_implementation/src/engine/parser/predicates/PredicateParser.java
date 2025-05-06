package engine.parser.predicates;

import engine.complex.predicate.Predicate;

import java.util.Optional;

public class PredicateParser {

    public static Optional<Predicate> parse(String input) {
        Optional<Predicate> predicate = WriteParser.parse(input).map(w -> w);

        if (predicate.isEmpty()) {
            predicate = ReadParser.parse(input).map(r -> r);
        }

        if (predicate.isEmpty()) {
            predicate = NLParser.parse(input).map(n -> n);
        }

        if (predicate.isEmpty()) {
            predicate = FailParser.parse(input).map(f -> f);
        }

        return predicate;
    }
}
