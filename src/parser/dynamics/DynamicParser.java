package parser.dynamics;

import interpreter.Fact;
import interpreter.Term;
import interpreter.complex.dynamic.*;
import parser.RuleParser;
import parser.TermParser;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DynamicParser {

    private static Fact parseDynamicArg(String input, boolean termOnly) {
        String argString = input.substring(input.indexOf("(") + 1, input.lastIndexOf(")"));

        Fact arg;
        if (argString.charAt(0) == '(' && argString.charAt(argString.length() - 1) == ')') {
            if (termOnly) {
                throw new RuntimeException("Rule is not allowed as argument for retract(all) " + argString);
            }

            arg = RuleParser.parseRule(argString.substring(1, argString.length() - 1));
        } else {
            Optional<Term> term = TermParser.parseWithComplexTermPriority(argString);

            if (term.isEmpty()) {
                throw new RuntimeException("Could not parse " + argString);
            }

            arg = term.get();
        }

        return arg;
    }

    private static Term parseRetractArg(String input) {
        return (Term)parseDynamicArg(input, true);
    }

    private static Fact parseAssertArg(String input) {
        return parseDynamicArg(input, false);
    }

    public static Optional<Dynamic> parseAsserta(String input) {
        if (!Asserta.isAsserta(input)) {
            return Optional.empty();
        }

        Fact arg = parseAssertArg(input);

        return Optional.of(new Asserta(arg));
    }

    public static Optional<Dynamic> parseAssertz(String input) {
        if (!Assertz.isAssertz(input)) {
            return Optional.empty();
        }

        Fact arg = parseAssertArg(input);

        return Optional.of(new Assertz(arg));
    }

    public static Optional<Dynamic> parseAssert(String input) {
        if (!Assert.isAssert(input)) {
            return Optional.empty();
        }

        Fact arg = parseAssertArg(input);

        return Optional.of(new Assert(arg));
    }

    public static Optional<Dynamic> parseRetract(String input) {
        if (!Retract.isRetract(input)) {
            return Optional.empty();
        }

        Term arg = parseRetractArg(input);

        return Optional.of(new Retract(arg));
    }

    public static Optional<Dynamic> parseRetractall(String input) {
        if (!Retractall.isRetractall(input)) {
            return Optional.empty();
        }

        Term arg = parseRetractArg(input);

        return Optional.of(new Retractall(arg));
    }

    public static Optional<Dynamic> parse(String input) {
        List<Function<String, Optional<Dynamic>>> parsers = List.of(
                DynamicParser::parseAsserta,
                DynamicParser::parseAssertz,
                DynamicParser::parseAssert,
                DynamicParser::parseRetract,
                DynamicParser::parseRetractall
        );

        return parsers.stream()
                .map(parser -> parser.apply(input))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}
