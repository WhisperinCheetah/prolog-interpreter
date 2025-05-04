package src.parser;

import src.Fact;
import src.Structure;
import src.Term;
import src.complex.ComplexTerm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StructureParser {

    public static Structure parseStructure(String input) {
        String[] headAndBody = input.split(":-");
        ComplexTerm head = ComplexTermParser.parseComplexTerm(headAndBody[0]);

        ArrayList<String> bodyParts = Parser.splitByComma(headAndBody[1]);
        List<Fact> body = bodyParts.stream().map(s -> (Fact)ComplexTermParser.parseComplexTerm(s)).toList();

        return new Structure(head, body);
    }

    public static Optional<Structure> parse(String input) {
        if (Structure.isRule(input)) {
            return Optional.of(parseStructure(input));
        }

        return Optional.empty();
    }
}
