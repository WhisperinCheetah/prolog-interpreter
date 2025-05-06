package engine.parser;

import engine.Term;
import engine.complex.ComplexTerm;
import engine.parser.dynamics.DynamicParser;
import engine.parser.predicates.PredicateParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComplexTermParser {


    private static Term getOrFail(Optional<Term> term) {
        if (term.isEmpty()) throw new AssertionError("The given term should not be empty");

        return term.get();
    }

    // TODO
    public static ComplexTerm parseComplexTerm(String input) {
        if (!input.contains("(")) {
            return new ComplexTerm(input);
        }

        String functor = input.substring(0, input.indexOf('('));
        String argsString = input.substring(input.indexOf('(') + 1, input.lastIndexOf(')'));

        ArrayList<String> argsStringList = Parser.splitByComma(argsString);
        List<Term> args = argsStringList.stream().map(TermParser::parse).map(ComplexTermParser::getOrFail).toList();

        return new ComplexTerm(functor, args);
    }

    public static Optional<ComplexTerm> parseComplexTermOptional(String input) {
        if (ComplexTerm.isComplexTerm(input)) {
            return Optional.of(parseComplexTerm(input));
        }

        return Optional.empty();
    }

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
