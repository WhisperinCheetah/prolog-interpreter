package src.parser;

import src.Structure;
import src.clauses.Clause;
import src.clauses.Fact;
import src.clauses.Rule;

import java.util.Optional;

public class ClauseParser {

    public static Optional<Clause> parseClause(String line) {
        if (Rule.isRule(line)) {
            return Optional.of(Rule.fromString(line));
        } else if (Fact.isFact(line)) {
            return Optional.of(Fact.fromString(line));
        }

        return Optional.empty();
    }
}
