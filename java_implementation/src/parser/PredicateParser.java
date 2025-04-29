package src.parser;

import src.predicates.Predicate;
import src.predicates.Write;

import java.util.Optional;

public class PredicateParser {

    public static Optional<Predicate> parsePredicate(String line) {
        if (Write.isWrite(line)) {
            return Optional.of(Write.fromString(line));
        }

        return Optional.empty();
    }
}
