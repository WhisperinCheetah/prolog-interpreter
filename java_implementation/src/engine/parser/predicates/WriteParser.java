package engine.parser.predicates;

import engine.Term;
import engine.complex.Write;
import engine.parser.TermParser;

import java.util.Optional;

public class WriteParser {

    private static Write parseWrite(String input) {
        String argString = input.substring(input.indexOf('(') + 1, input.indexOf(')'));

        Optional<Term> arg = TermParser.parse(argString);

        if (arg.isEmpty()) {
            throw new RuntimeException("Could not parse argument '" + argString + "'");
        }

        return new Write(arg.get());
    }

    public static Optional<Write> parse(String input) {
        if (Write.isWrite(input)) {
            return Optional.of(parseWrite(input));
        }

        return Optional.empty();
    }
}
