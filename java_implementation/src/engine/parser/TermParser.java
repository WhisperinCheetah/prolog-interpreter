package engine.parser;

import engine.Term;

import java.util.Optional;

public class TermParser {

    public static Optional<Term> parseWithComplexTermPriority(String input) {
        Optional<Term> term = ComplexTermParser.parse(input).map(s -> s);

        if (term.isEmpty()) {
            term = SimpleTermParser.parse(input).map(s -> s);
        }

        return term;
    }

    public static Optional<Term> parse(String input) {
        Optional<Term> term = SimpleTermParser.parse(input).map(s -> s);

        if (term.isEmpty()) {
            term = ComplexTermParser.parse(input).map(s -> s);
        }

        return term;
    }
}
