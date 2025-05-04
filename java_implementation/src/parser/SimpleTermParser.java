package src.parser;

import src.simple.SimpleTerm;

import java.util.Optional;

public class SimpleTermParser {

    public static Optional<SimpleTerm> parse(String input) {
        Optional<SimpleTerm> term = AtomParser.parse(input).map(a -> a);

        if (term.isEmpty()) {
            term = VariableParser.parse(input).map(a -> a);
        }

        if (term.isEmpty()) {
            term = NumberParser.parse(input).map(a -> a);
        }

        return term;
    }
}
