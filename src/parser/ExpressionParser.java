package parser;

import interpreter.Term;
import interpreter.complex.expression.*;
import parser.simples.NumberParser;
import parser.simples.VariableParser;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ExpressionParser {

    private static List<Term> parseExpressionArgs(String input) {
        String argString = input.substring(input.indexOf("(") + 1, input.lastIndexOf(")"));
        List<String> argStrings = Parser.splitByComma(argString);

        if (argStrings.size() != 2) {
            throw new IllegalArgumentException("Invalid expression syntax: " + argString);
        }

        Optional<EvaluableExpression> maybeArgl = ExpressionParser.parse(argStrings.getFirst());
        Optional<EvaluableExpression> maybeArgr = ExpressionParser.parse(argStrings.getLast());

        if (maybeArgl.isEmpty()) throw new IllegalArgumentException("Invalid left hand argument in expression: " + argStrings.getFirst());
        if (maybeArgr.isEmpty()) throw new IllegalArgumentException("Invalid right hand argument in expression: " + argStrings.getLast());


        EvaluableExpression argl = maybeArgl.get();
        EvaluableExpression argr = maybeArgr.get();

        return List.of((Term)argl, (Term)argr);
    }

    private static Optional<EvaluableExpression> parseBinaryExpression(
            String input,
            Function<String, Boolean> test,
            BiFunction<Term, Term, BinaryExpression> constructor
    ) {
        if (!test.apply(input)) {
            return Optional.empty();
        }

        List<Term> args = parseExpressionArgs(input);

        return Optional.of(constructor.apply(args.getFirst(), args.getLast()));
    }

    public static Optional<EvaluableExpression> parseAddition(String input) {
        return parseBinaryExpression(input, Addition::isAddition, Addition::new);
    }

    public static Optional<EvaluableExpression> parseSubtraction(String input) {
        return parseBinaryExpression(input, Subtraction::isSubtraction, Subtraction::new);
    }

    public static Optional<EvaluableExpression> parseMultiplication(String input) {
        return parseBinaryExpression(input, Multiplication::isMultiplication, Multiplication::new);
    }

    public static Optional<EvaluableExpression> parseDivision(String input) {
        return parseBinaryExpression(input, Division::isDivision, Division::new);
    }


    /**
     * Tries to parse an expression, which occurs on the right side of is/2, in following order:
     * 1. +
     * 2. -
     * 3. *
     * 4. /
     * 5. Variable
     * 6. Number
     *
     * @param input A line of input
     * @return An EvaluableExpression
     */
    public static Optional<EvaluableExpression> parse(String input) {
        List<Function<String, Optional<EvaluableExpression>>> parsers = List.of(
                ExpressionParser::parseAddition,
                ExpressionParser::parseSubtraction,
                ExpressionParser::parseMultiplication,
                ExpressionParser::parseDivision,
                s -> VariableParser.parse(s).map(v -> v),
                s -> NumberParser.parse(s).map(v -> v)
        );

        return Parser.parseStack(input, parsers);
    }
}
