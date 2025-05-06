package engine.parser.predicates;

import engine.complex.NL;

import java.util.Optional;

public class NLParser {

    public static Optional<NL> parse(String input) {
        if (NL.isNL(input)) {
            return Optional.of(new NL());
        }

        return Optional.empty();
    }
}
