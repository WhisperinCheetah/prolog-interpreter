package engine.parser.dynamics;

import engine.Fact;
import engine.Term;
import engine.complex.Asserta;
import engine.parser.RuleParser;
import engine.parser.TermParser;

import java.util.Optional;

public class AssertaParser {

    public static Asserta parseAsserta(String input) {
        String argString = input.substring(input.indexOf("(") + 1, input.lastIndexOf(")"));

        Fact arg;
        if (argString.charAt(0) == '(' && argString.charAt(argString.length() - 1) == ')') {
            arg = RuleParser.parseRule(argString.substring(1, argString.length() - 1));
        } else {
            Optional<Term> term = TermParser.parseWithComplexTermPriority(argString);

            if (term.isEmpty()) {
                throw new RuntimeException("Could not parse " + argString);
            }

            arg = term.get();
        }

        return new Asserta(arg);
    }

    public static Optional<Asserta> parse(String input) {
        if (Asserta.isAsserta(input)) {
            return Optional.of(parseAsserta(input));
        }

        return Optional.empty();
    }
}
