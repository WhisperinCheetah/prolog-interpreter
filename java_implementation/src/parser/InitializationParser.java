package src.parser;

import src.Structure;
import src.directives.Initialization;

import java.util.Optional;

public class InitializationParser {

    public static Optional<Initialization> parseInitialization(String line) {
        if (!line.matches(":-initialization\\(.*\\)")) {
            return Optional.empty();
        }

        String goalString = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
        Optional<Structure> goal = StructureParser.parseStructure(goalString);

        return goal.map(Initialization::new);
    }
}
