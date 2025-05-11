package parser;

import interpreter.Term;
import interpreter.complex.ComplexTerm;
import parser.dynamics.DynamicParser;
import parser.predicates.PredicateParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComplexTermParser {


    public static ComplexTerm parseComplexTerm(String input) {
        if (!input.contains("(")) {
            return new ComplexTerm(input);
        }

        String functor = input.substring(0, input.indexOf('('));
        String argsString = input.substring(input.indexOf('(') + 1, input.lastIndexOf(')'));

        List<String> argsStringList = Parser.splitByComma(argsString);
        List<Optional<Term>> maybeArgs = argsStringList.stream().map(TermParser::parse).toList();
        List<Term> args = new ArrayList<>();
        for (int i = 0; i < maybeArgs.size(); i++) {
            Optional<Term> term = maybeArgs.get(i);
            if (term.isEmpty()) throw new AssertionError("Parsed argument returned empty: " + argsStringList.get(i));

            args.add(term.get());
        }

        return new ComplexTerm(functor, args);
    }

    public static Optional<ComplexTerm> parseComplexTermOptional(String input) {
        if (ComplexTerm.isComplexTerm(input)) {
            return Optional.of(parseComplexTerm(input));
        }

        return Optional.empty();
    }


    /**
     * Tries to parse in the following order:
     * 1. Predicate
     * 2. Dynamic
     * 3. Base ComplexTerm
     *
     * @param input A line of input
     * @return A ComplexTerm if the line is a ComplexTerm, otherwise empty
     */
    public static Optional<ComplexTerm> parse(String input) {
        Optional<ComplexTerm> term = PredicateParser.parse(input).map(p -> p);

        if (term.isEmpty()) {
            term = DynamicParser.parse(input).map(p -> p);
        }

        if (term.isEmpty()) {
            term = parseComplexTermOptional(input);
        }

        return term;
    }
}
