package parser;

import interpreter.Term;
import parser.simples.SimpleTermParser;

import java.util.Optional;

/**
 * TermParser's functions try to parse a ComplexTerm or a SimpleTerm (look at function for order)
 */
public class TermParser {

    /**
     * Tries to parse a ComplexTerm and then a SimpleTerm
     *
     * @param input the input
     * @return maybe a Term
     */
    public static Optional<Term> parseWithComplexTermPriority(String input) {
        Optional<Term> term = ComplexTermParser.parse(input).map(s -> s);

        if (term.isEmpty()) {
            term = SimpleTermParser.parse(input).map(s -> s);
        }

        return term;
    }

    /**
     * Tries to parse a SimpleTerm and then a ComplexTerm
     *
     * @param input the input
     * @return maybe a Term
     */
    public static Optional<Term> parse(String input) {
        Optional<Term> term = SimpleTermParser.parse(input).map(s -> s);

        if (term.isEmpty()) {
            term = ComplexTermParser.parse(input).map(s -> s);
        }

        return term;
    }
}
