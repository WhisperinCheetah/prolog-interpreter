package parser.predicates;

import interpreter.complex.predicate.Fail;
import interpreter.complex.predicate.Predicate;

import java.util.Optional;

public class FailParser {

    public static Optional<Predicate> parse(String input) {
        if (Fail.isFail(input)) {
            return Optional.of(new Fail());
        }

        return Optional.empty();
    }
}
