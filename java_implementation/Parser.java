import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {

    final String path;

    public Parser(String path) {
        this.path = path;
    }

    public static Term parseChunk(String line) {
        if (Variable.isVariable(line)) {
            return new Variable(line);
        } else if (Fact.isStructure(line)) {
            return Fact.fromString(line);
        } else if (Atom.isAtom(line)) {
            return new Atom(line);
        } else if (Rule.isRule(line)) {
            return Rule.fromString(line);
        } else {
            return Number.fromString(line);
        }
    }

    // TODO parse by . but ignore "" and numbers
    public List<Term> parse() throws IOException {
        String program = new String(Files.readAllBytes(Paths.get(path))).replaceAll("\\s+","");
        ArrayList<String> lines = new ArrayList<>(Arrays.asList(program.split("\\.")));

        return lines.stream().map(Parser::parseChunk).toList();
    }
}
