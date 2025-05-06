package engine.parser.predicates;

import engine.Term;
import engine.complex.predicate.Read;
import engine.parser.TermParser;

import java.util.Optional;

public class ReadParser {

    public static Read parseRead(String input) {
        String argString = input.substring(input.indexOf("(") + 1, input.indexOf(")"));

        Optional<Term> maybeArg = TermParser.parse(argString);

        if (maybeArg.isEmpty()) {
            throw new RuntimeException("Could not parse argument " + argString);
        }

        Term arg = maybeArg.get();

        return new Read(arg);
    }

    public static Optional<Read> parse(String input) {
        if (Read.isRead(input)) {
            return Optional.of(parseRead(input));
        }

        return Optional.empty();
    }
}
