package engine.parser.predicates;

import engine.Fact;
import engine.Term;
import engine.complex.dynamic.Dynamic;
import engine.complex.predicate.Predicate;
import engine.complex.predicate.Unify;
import engine.parser.Parser;
import engine.parser.RuleParser;
import engine.parser.TermParser;
import engine.parser.dynamics.DynamicParser;

import java.util.List;
import java.util.Optional;
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

    private static Optional<Predicate> parseUnify(String input) {
        if (!Unify.isUnify(input)) {
            return Optional.empty();
        }

        List<Term> args = parseOperatorArgs(input);

        return Optional.of(new Unify(args.getFirst(), args.getLast()));
    }

    public static Optional<Predicate> parse(String input) {
        List<Function<String, Optional<Predicate>>> parsers = List.of(
                OperatorParser::parseUnify
        );

        return parsers.stream()
                .map(parser -> parser.apply(input))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}
