package src.parser;

import src.Rule;
import src.Term;
import src.complex.ComplexTerm;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StructureParser {

    public static Rule parseStructure(String input) {
        String[] headAndBody = input.split(":-");
        ComplexTerm head = ComplexTermParser.parseComplexTerm(headAndBody[0]);

        ArrayList<String> bodyParts = Parser.splitByComma(headAndBody[1]);
        List<Term> body = new ArrayList<>();
        for (String bodyPart : bodyParts) {
            Optional<Term> term = TermParser.parse(bodyPart);

            try {
                if (term.isEmpty()) {
                    throw new ParseException("Could not parse " + input, 0);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            body.add(term.get());
        }

        return new Rule(head, body);
    }

    public static Optional<Rule> parse(String input) {
        if (Rule.isRule(input)) {
            return Optional.of(parseStructure(input));
        }

        return Optional.empty();
    }
}
