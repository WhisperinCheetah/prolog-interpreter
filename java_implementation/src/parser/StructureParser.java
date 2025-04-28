package src.parser;

import src.Structure;
import src.clauses.Clause;

import java.util.Optional;

public class StructureParser {

    public static Optional<Structure> parseStructure(String line) {
        Optional<Structure> structure = PredicateParser.parsePredicate(line).map(Structure.class::cast);

        if (structure.isEmpty()) {
            structure = ClauseParser.parseClause(line).map(Structure.class::cast);
        }

        return structure;
    }
}
