package src.parser;

import src.Structure;
import src.Term;
import src.TermDatabase;
import src.clauses.Clause;
import src.clauses.Fact;
import src.clauses.Rule;
import src.directives.Initialization;
import src.predicates.Predicate;
import src.simples.Atom;
import src.simples.Number;
import src.simples.Variable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class Parser {

    final String path;

    public Parser(String path) {
        this.path = path;
    }

    public static Term parseChunk(String line) {
        if (Variable.isVariable(line)) {
            return new Variable(line);
        } else if (Fact.isFact(line)) {
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
    public TermDatabase parseProgram(boolean verbose) throws IOException {
        String program = new String(Files.readAllBytes(Paths.get(path))).replaceAll("\\s+","");
        ArrayList<String> lines = new ArrayList<>(Arrays.asList(program.split("\\.")));

        System.out.println("Program: " + program);

        TermDatabase db = new TermDatabase();

        for (String line : lines) {
            Optional<Initialization> init = InitializationParser.parseInitialization(line);

            if (init.isPresent()) {
                db.addInitialization(init.get());
                continue;
            }

            Optional<Clause> structure = ClauseParser.parseClause(line);

            if (structure.isPresent()) {
                db.addClause(structure.get());
                continue;
            }

            System.out.println("Unknown statement: " + line);
        }

        db.finalizeDatabase();

        if (verbose) {
            System.out.println("Parsed " + db.size() + " clauses");
            System.out.println(db.getClauses());
        }

        return db;
    }
}
