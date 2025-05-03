package src.directives;

import src.Structure;
import src.Term;
import src.TermDatabase;
import src.parser.StructureParser;

import java.util.List;
import java.util.Optional;

public class Initialization extends Directive {
    public Initialization(Structure goal) {
        super(goal);
    }

    public void run(TermDatabase db) {
        goal.execute(db);
    }

    public static boolean isInitialization(String line) {
        return line.matches(":-initialization\\(.*\\)");
    }

    public static Initialization fromString(String line) {
        String goalString = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
        Optional<Structure> goal = StructureParser.parseStructure(goalString);

        if (goal.isEmpty()) {
            throw new AssertionError("Initialization goal expected callable but got " + goalString);
        }

        return new Initialization(goal.get());
    }
}
