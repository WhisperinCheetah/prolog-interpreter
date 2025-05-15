package parser.predicates;

import interpreter.Term;
import interpreter.complex.expression.EvaluableExpression;
import interpreter.complex.predicate.*;
import interpreter.complex.predicate.builtins.*;
import parser.ExpressionParser;
import parser.Parser;
import parser.TermParser;
import parser.simples.NumberParser;
import parser.simples.VariableParser;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class PredicateParser {

    private static List<Term> parseIsArgs(String input) {
        String argString = input.substring(input.indexOf("(") + 1, input.lastIndexOf(")"));

        List<String> argsString = Parser.splitByComma(argString);

        if (argsString.size() != 2) {
            throw new IllegalArgumentException("Invalid number of arguments in is/2: " + argString);
        }

        Optional<Term> argl = VariableParser.parse(argsString.getFirst()).map(v -> v); // try variable
        if (argl.isEmpty()) { argl = NumberParser.parse(argsString.getFirst()).map(v -> v); } // try number instead
        Optional<EvaluableExpression> argr = ExpressionParser.parse(argsString.get(1));

        if (argl.isEmpty()) throw new IllegalArgumentException("Invalid argument in is/2: " + argsString.getFirst());
        if (argr.isEmpty()) throw new IllegalArgumentException("Invalid argument in is/2: " + argsString.getLast());

        return List.of(argl.get(), argr.get());
    }

    private static Term parseArg(String input) {
        Optional<Term> arg = TermParser.parse(input);

        if (arg.isEmpty()) throw new IllegalArgumentException("Invalid argument: " + arg);

        return arg.get();
    }

    private static List<Term> parseArgs(String input, int n) {
        String argString = input.substring(input.indexOf("(") + 1, input.lastIndexOf(")"));

        List<String> argStrings = Parser.splitByComma(argString);

        if (argStrings.size() != n) {
            throw new IllegalArgumentException("Invalid number of arguments: found " + argStrings.size() + " but expected " + n);
        }

        List<Term> args = argStrings.stream().map(PredicateParser::parseArg).toList();

        return args;
    }


    public static Optional<Predicate> parseFail(String input) {
        if (Fail.isFail(input)) {
            return Optional.of(new Fail());
        }

        return Optional.empty();
    }

    public static Optional<Predicate> parseIs(String input) {
        if (!Is.isIs(input)) {
            return Optional.empty();
        }

        List<Term> args = parseIsArgs(input);

        return Optional.of(new Is(args));
    }

    public static Optional<Predicate> parseSucc(String input) {
        if (!Succ.isSucc(input)) {
            return Optional.empty();
        }

        List<Term> args = parseArgs(input, 2);

        return Optional.of(new Succ(args));
    }

    public static Optional<Predicate> parseBetween(String input) {
        if (!Between.isBetween(input)) {
            return Optional.empty();
        }

        List<Term> args = parseArgs(input, 3);

        return Optional.of(new Between(args));
    }

    public static Optional<Predicate> parseCut(String input) {
        if (!Cut.isCut(input)) {
            return Optional.empty();
        }

        return Optional.of(new Cut());
    }


    /**
     * Tries to parse following predicates in order:
     * 1. Write
     * 2. Read
     * 3. NL (Newline)
     * 4. Fail
     * 6. An Operator (+, -, *, /)
     * 7. Is
     * 8. Succ
     * 9. Between
     * 10. Cut
     *
     * @param input A line of input
     * @return A Predicate if the line is a predicate
     */
    public static Optional<Predicate> parse(String input) {
        List<Function<String, Optional<Predicate>>> functionStack = List.of(
                IOPredicateParser::parse,
                PredicateParser::parseFail,
                OperatorParser::parse,
                PredicateParser::parseIs,
                PredicateParser::parseSucc,
                PredicateParser::parseBetween,
                PredicateParser::parseCut
        );

        return Parser.parseStack(input, functionStack);
    }
}
