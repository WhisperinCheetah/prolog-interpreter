package engine.parser.predicates;

import engine.Term;
import engine.complex.expression.EvaluableExpression;
import engine.complex.predicate.Between;
import engine.complex.predicate.Is;
import engine.complex.predicate.Predicate;
import engine.complex.predicate.Succ;
import engine.parser.ExpressionParser;
import engine.parser.Parser;
import engine.parser.TermParser;
import engine.parser.simples.VariableParser;
import engine.simple.Variable;

import java.util.List;
import java.util.Optional;

public class PredicateParser {

    private static List<Term> parseIsArgs(String input) {
        String argString = input.substring(input.indexOf("(") + 1, input.lastIndexOf(")"));

        List<String> argsString = Parser.splitByComma(argString);

        if (argsString.size() != 2) {
            throw new IllegalArgumentException("Invalid number of arguments in is/2: " + argString);
        }

        Optional<Variable> argl = VariableParser.parse(argsString.getFirst());
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

    public static Optional<Predicate> parse(String input) {
        Optional<Predicate> predicate = WriteParser.parse(input).map(w -> w);

        if (predicate.isEmpty()) {
            predicate = ReadParser.parse(input).map(r -> r);
        }

        if (predicate.isEmpty()) {
            predicate = NLParser.parse(input).map(n -> n);
        }

        if (predicate.isEmpty()) {
            predicate = FailParser.parse(input).map(f -> f);
        }

        if (predicate.isEmpty()) {
            predicate = OperatorParser.parse(input);
        }

        if (predicate.isEmpty()) {
            predicate = parseIs(input);
        }

        if (predicate.isEmpty()) {
            predicate = parseSucc(input);
        }

        if (predicate.isEmpty()) {
            predicate = parseBetween(input);
        }

        return predicate;
    }
}
