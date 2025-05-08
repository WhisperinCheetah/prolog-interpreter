package engine.parser;

import engine.Term;
import engine.complex.expression.*;
import engine.complex.predicate.Predicate;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ExpressionParser {

    private static List<Expression> parseExpressionArgs(String input) {
        String argString = input.substring(input.indexOf("("), input.lastIndexOf(")"));
        List<String> argStrings = Parser.splitByComma(argString);

        if (argStrings.size() != 2) {
            throw new IllegalArgumentException("Invalid expression syntax: " + argString);
        }

        Optional<Expression> maybeArgl = ExpressionParser.parse(argStrings.getFirst());
        Optional<Expression> maybeArgr = ExpressionParser.parse(argStrings.getLast());

        if (maybeArgl.isEmpty() || maybeArgr.isEmpty()) {
            throw new IllegalArgumentException("Invalid expression syntax: " + argString);
        }

        Expression argl = maybeArgl.get();
        Expression argr = maybeArgr.get();

        return List.of(argl, argr);
    }

    private static Optional<Expression> parseBinaryExpression(
            String input,
            Function<String, Boolean> test,
            BiFunction<Expression, Expression, Expression> constructor
    ) {
        if (!test.apply(input)) {
            return Optional.empty();
        }

        List<Expression> args = parseExpressionArgs(input);

        return Optional.of(constructor.apply(args.getFirst(), args.getLast()));
    }

    public static Optional<Expression> parseAddition(String input) {
        return parseBinaryExpression(input, Addition::isAddition, Addition::new);
    }

    public static Optional<Expression> parseSubtraction(String input) {
        return parseBinaryExpression(input, Subtraction::isSubtraction, Subtraction::new);
    }

    public static Optional<Expression> parseMultiplication(String input) {
        return parseBinaryExpression(input, Multiplication::isMultiplication, Multiplication::new);
    }

    public static Optional<Expression> parseDivision(String input) {
        return parseBinaryExpression(input, Division::isDivision, Division::new);
    }

    public static Optional<Expression> parse(String input) {
        List<Function<String, Optional<Expression>>> parsers = List.of(
                ExpressionParser::parseAddition,
                ExpressionParser::parseSubtraction,
                ExpressionParser::parseMultiplication,
                ExpressionParser::parseDivision
        );

        return Parser.parseStack(input, parsers);
    }
}
