package src.parser;

import src.Term;

import java.util.Optional;

public class TermParser {
    public static Optional<Term> parse(String input) {
        Optional<Term> term = SimpleTermParser.parse(input).map(s -> s);

        if (term.isEmpty()) {
            term = ComplexTermParser.parse(input).map(s -> s);
        }

        return term;
    }
}
