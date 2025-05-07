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
import java.util.function.Function;

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

    public static String convertToPrefix(String input) {
        String[] operators = {"==", "\\==", "=", "\\="};
        int parenDepth = 0;
        boolean inQuotes = false;
        char quoteChar = '\0';

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            // Handle quotes
            if ((c == '\'' || c == '"') && (i == 0 || input.charAt(i - 1) != '\\')) {
                if (inQuotes && c == quoteChar) {
                    inQuotes = false;
                } else if (!inQuotes) {
                    inQuotes = true;
                    quoteChar = c;
                }
            }

            if (inQuotes) continue;

            // Track parentheses
            if (c == '(') parenDepth++;
            else if (c == ')') parenDepth--;

            // Only search for operators at top level (outside any nested structures)
            if (parenDepth == 0) {
                for (String op : operators) {
                    if (input.startsWith(op, i)) {
                        String lhs = input.substring(0, i).trim();
                        String rhs = input.substring(i + op.length()).trim();
                        return op + "(" + lhs + "," + rhs + ")";
                    }
                }
            }
        }

        return input; // No top-level operator found; return as-is
    }

    public static String cleanString(String input) {
        List<Function<String, String>> functionStack = List.of(
                s -> s.charAt(s.length() - 1) == '.' ? s.substring(0, s.length() - 1) : s, // remove '.'
                s -> s.replaceAll("\\s&&[^ ]+", ""), // remove non-space whitespace
                s -> s.replaceAll(" {2,}", " ").trim(), // remove duplicate spaces
                s -> s.replaceAll("^:- ?dynamic ([a-z]+/\\d+)", ":-dynamic($1)"), // add brackets to dynamic directive
                s -> s.replaceAll("\\s+", ""),
                Parser::convertToPrefix // convert operators to prefix
        );

        return functionStack.stream()
                .reduce(Function.identity(), Function::andThen)
                .apply(input);
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
