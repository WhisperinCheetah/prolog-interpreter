package parser.predicates;

import interpreter.Term;
import interpreter.complex.predicate.NL;
import interpreter.complex.predicate.Predicate;
import interpreter.complex.predicate.Read;
import interpreter.complex.predicate.Write;
import parser.Parser;
import parser.TermParser;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class IOPredicateParser {

    public static Optional<Predicate> parseWrite(String input) {
        if (!Write.isWrite(input)) {
            return Optional.empty();
        }

        String argString = input.substring(input.indexOf('(') + 1, input.lastIndexOf(')'));

        Optional<Term> arg = TermParser.parse(argString);

        if (arg.isEmpty()) {
            throw new RuntimeException("Could not parse argument '" + argString + "'");
        }

        return Optional.of(new Write(arg.get()));
    }

    public static Optional<Predicate> parseRead(String input) {
        if (!Read.isRead(input)) {
            return Optional.empty();
        }

        String argString = input.substring(input.indexOf("(") + 1, input.lastIndexOf(")"));

        Optional<Term> maybeArg = TermParser.parse(argString);

        if (maybeArg.isEmpty()) {
            throw new RuntimeException("Could not parse argument " + argString);
        }

        Term arg = maybeArg.get();

        return Optional.of(new Read(arg));
    }

    public static Optional<Predicate> parseNL(String input) {
        if (NL.isNL(input)) {
            return Optional.of(new NL());
        }

        return Optional.empty();
    }

    public static Optional<Predicate> parse(String input) {
        List<Function<String, Optional<Predicate>>> functionStack = List.of(
                IOPredicateParser::parseWrite,
                IOPredicateParser::parseRead,
                IOPredicateParser::parseNL
        );

        return Parser.parseStack(input, functionStack);
    }
}
