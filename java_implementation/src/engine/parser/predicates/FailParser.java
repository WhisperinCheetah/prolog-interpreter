package engine.parser.predicates;

import engine.complex.Fail;

import java.util.Optional;

public class FailParser {

    public static Optional<Fail> parse(String input) {
        if (Fail.isFail(input)) {
            return Optional.of(new Fail());
        }

        return Optional.empty();
    }
}
