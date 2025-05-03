package src.parser;

import src.predicates.NL;
import src.predicates.Predicate;
import src.predicates.Write;

import java.util.Optional;

public class PredicateParser {

    public static Optional<Predicate> parsePredicate(String line) {
        if (Write.isWrite(line)) {
            return Optional.of(Write.fromString(line));
        } else if (NL.isNL(line)) {
            return Optional.of(NL.fromString(line));
        }

        return Optional.empty();
    }
}
