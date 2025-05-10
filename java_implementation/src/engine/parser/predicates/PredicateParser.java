package engine.parser.predicates;

import engine.Term;
import engine.complex.expression.EvaluableExpression;
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

    private static List<Term> parseSuccArgs(String input) {
        String argString = input.substring(input.indexOf("(") + 1, input.lastIndexOf(")"));

        List<String> argStrings = Parser.splitByComma(argString);

        if (argStrings.size() != 2) {
            throw new IllegalArgumentException("Invalid number of arguments in succ/2: " + argString);
        }

        Optional<Term> argl = TermParser.parse(argStrings.get(1));
        Optional<Term> argr = TermParser.parse(argStrings.get(2));

        if (argl.isEmpty()) throw new IllegalArgumentException("Invalid argument in is/2: " + argStrings.getFirst());
        if (argr.isEmpty()) throw new IllegalArgumentException("Invalid argument in is/2: " + argStrings.getLast());

        return List.of(argl.get(), argr.get());
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

        List<Term> args = parseSuccArgs(input);

        return Optional.of(new Succ(args));
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

        return predicate;
    }
}
