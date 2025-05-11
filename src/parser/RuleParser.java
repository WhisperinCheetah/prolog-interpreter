package parser;

import interpreter.Rule;
import interpreter.Term;
import interpreter.complex.ComplexTerm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RuleParser {

    public static Rule parseRule(String input) {
        String[] headAndBody = input.split(":-");
        Optional<ComplexTerm> maybeHead = ComplexTermParser.parse(headAndBody[0]);

        if (maybeHead.isEmpty()) {
            throw new RuntimeException("Could not parse complex term: " + headAndBody[0]);
        }

        ComplexTerm head = maybeHead.get();

        List<String> bodyParts = Parser.splitByComma(headAndBody[1]);
        List<Term> body = new ArrayList<>();
        for (String bodyPart : bodyParts) {
            Optional<Term> term = TermParser.parseWithComplexTermPriority(bodyPart);

            if (term.isEmpty()) {
                throw new RuntimeException("Could not parse " + bodyPart + " from " + input);
            }

            body.add(term.get());
        }

        return new Rule(head, body);
    }

    public static Optional<Rule> parse(String input) {
        if (Rule.isRule(input)) {
            return Optional.of(parseRule(input));
        }

        return Optional.empty();
    }
}
