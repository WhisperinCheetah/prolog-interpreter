package src.parser;

import src.Structure;
import src.directives.Initialization;

import java.util.Optional;

public class InitializationParser {
    public static Optional<Initialization> parseInitialization(String line) {
        if (!Initialization.isInitialization(line)) {
            return Optional.empty();
        }

        return Optional.of(Initialization.fromString(line));
    }
}
