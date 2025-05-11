package parser.predicates;

import interpreter.Term;
import interpreter.complex.predicate.Predicate;
import interpreter.complex.predicate.operator.Equality;
import interpreter.complex.predicate.operator.NotEquality;
import interpreter.complex.predicate.operator.NotUnify;
import interpreter.complex.predicate.operator.Unify;
import parser.Parser;
import parser.TermParser;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class OperatorParser {

    private static List<Term> parseOperatorArgs(String input) {
        String argString = input.substring(input.indexOf("(") + 1, input.lastIndexOf(")"));

        List<String> argsString = Parser.splitByComma(argString);

        if (argsString.size() != 2) {
            throw new RuntimeException("Invalid number of arguments in operator expression: " + argString);
        }

        Optional<Term> argl = TermParser.parseWithComplexTermPriority(argsString.getFirst());
        Optional<Term> argr = TermParser.parseWithComplexTermPriority(argsString.getLast());

        if (argl.isEmpty() || argr.isEmpty()) {
            throw new RuntimeException("Could not parse " + argsString);
        }

        return List.of(argl.get(), argr.get());
    }

    private static Optional<Predicate> parseBinaryOperator(
            String input,
            Function<String, Boolean> test,
            BiFunction<Term, Term, Predicate> constructor
    ) {
        if (!test.apply(input)) {
            return Optional.empty();
        }

        List<Term> args = parseOperatorArgs(input);
        return Optional.of(constructor.apply(args.getFirst(), args.getLast()));
    }

    private static Optional<Predicate> parseUnify(String input) {
        return parseBinaryOperator(input, Unify::isUnify, Unify::new);
    }

    private static Optional<Predicate> parseNotUnify(String input) {
        return parseBinaryOperator(input, NotUnify::isNotUnify, NotUnify::new);
    }

    private static Optional<Predicate> parseEquality(String input) {
        return parseBinaryOperator(input, Equality::isEquality, Equality::new);
    }

    private static Optional<Predicate> parseNotEquality(String input) {
        return parseBinaryOperator(input, NotEquality::isNotEquality, NotEquality::new);
    }

    public static Optional<Predicate> parse(String input) {
        List<Function<String, Optional<Predicate>>> parsers = List.of(
                OperatorParser::parseUnify,
                OperatorParser::parseNotUnify,
                OperatorParser::parseEquality,
                OperatorParser::parseNotEquality
        );

        return Parser.parseStack(input, parsers);
    }
}
