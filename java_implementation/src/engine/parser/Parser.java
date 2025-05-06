package engine.parser;

import engine.Fact;
import engine.TermDatabase;
import engine.directives.DynamicDirective;
import engine.directives.Initialization;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Parser {

    final String path;

    public Parser(String path) {
        this.path = path;
    }

    public static ArrayList<String> splitByComma(String input) {
        ArrayList<String> splitArgsStrings = new ArrayList<>();
        int openBracketCounter = 0;
        int lastSplit = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(') openBracketCounter++;
            if (input.charAt(i) == ')') openBracketCounter--;

            if (openBracketCounter == 0 && input.charAt(i) == ',') {
                splitArgsStrings.add(input.substring(lastSplit, i));
                lastSplit = i + 1;
            }
        }

        splitArgsStrings.add(input.substring(lastSplit));

        return splitArgsStrings;
    }

    public static String cleanString(String input) {
        String noDot = input.charAt(input.length() - 1) == '.' ? input.substring(0, input.length() - 1) : input;

        String noOtherWhitespace = noDot.replaceAll("\\s&&[^ ]+", "");
        String singleSpaced = noOtherWhitespace.replaceAll(" {2,}", " ").trim();

        String dynamicWithBrackets = singleSpaced.replaceAll("^:- ?dynamic ([a-z]+/\\d+)", ":-dynamic($1)");

        return dynamicWithBrackets.replaceAll("\\s+", "");
    }

    // TODO parse by . but ignore "" and numbers
    public TermDatabase parseProgram(boolean verbose) throws IOException {
        // String program = new String(Files.readAllBytes(Paths.get(path))).replaceAll("\\s+","");
        // ArrayList<String> lines = new ArrayList<>(Arrays.asList(program.split("\\.")));

        String dirtyProgram = new String(Files.readAllBytes(Paths.get(path)));
        List<String> lines = new ArrayList<>(Arrays.asList(dirtyProgram.split("\\."))).stream().map(Parser::cleanString).toList();

        TermDatabase db = new TermDatabase();

        for (String line : lines) {
            Optional<Initialization> initialization = InitializationParser.parse(line);

            if (initialization.isPresent()) {
                db.addInitialization(initialization.get());
                continue;
            }

            Optional<DynamicDirective> dynamicDirective = DynamicDirectiveParser.parse(line);

            if (dynamicDirective.isPresent()) {
                db.addDynamic(dynamicDirective.get());
                continue;
            }

            Optional<Fact> fact = RuleParser.parse(line).map(s -> s);

            if (fact.isEmpty()) {
                fact = ComplexTermParser.parse(line).map(s -> s);
            }

            if (fact.isEmpty()) {
                throw new AssertionError("Cannot parse " + line);
            }

            db.addFact(fact.get());
        }

        db.finalizeDatabase();

        if (verbose) {
            System.out.println("Parsed " + db.size() + " clauses");
            System.out.println(db.getFacts());
            System.out.println("Dynamic statements: " + db.getDynamicsList());
        }

        return db;
    }
}
