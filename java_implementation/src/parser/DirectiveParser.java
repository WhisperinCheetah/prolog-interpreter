package src.parser;

import src.clauses.Clause;
import src.directives.Directive;
import src.directives.Initialization;

import java.util.Optional;

public class DirectiveParser {
    public static Optional<Directive> parseDirective(String line) {
        if (Initialization.isInitialization(line)) {
            return Optional.of(Initialization.fromString(line));
        }

        return Optional.empty();
    }

}
