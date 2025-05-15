package parser;

import interpreter.Fact;
import interpreter.FactDatabase;
import interpreter.directives.DynamicDirective;
import interpreter.directives.Initialization;
import parser.directives.DynamicDirectiveParser;
import parser.directives.InitializationParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Parser {

    final String path;
    boolean verbose;

    /**
     * Constructor that takes a file and creates a new Parser with that file
     *
     * @param path Input file
     */
    public Parser(String path, boolean verbose) {
        this.path = path;
        this.verbose = verbose;

        if (path == null) {
            throw new IllegalArgumentException("No path provided");
        }
    }


    /**
     * Takes a list of functions that return an optional result of type T, applies them all and takes the result
     * of the first function that succeeded.
     *
     * @param input a line of input
     * @param parserStack the list of functions
     * @param <T> the return type
     * @return maybe a result of type T
     */
    public static <T> Optional<T> parseStack(String input, List<Function<String, Optional<T>>> parserStack) {
        return parserStack.stream()
                .map(parser -> parser.apply(input))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    /**
     * Splits a line by a certain character 'delimiter'. Does not split when between quotes or brackets.
     *
     * @param input a line of input
     * @param delimiter the character to be split by
     * @return a list of split lines
     */
    public static List<String> splitByChar(String input, char delimiter) {
        ArrayList<String> splitArgsStrings = new ArrayList<>();
        int openBracketCounter = 0;
        int lastSplit = 0;
        char quoteChar = '\0';
        boolean inQuotes = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (!inQuotes && (c == '\'' || c == '"')) {
                inQuotes = true;
                quoteChar = c;
            } else if (inQuotes && c == quoteChar) {
                inQuotes = false;
                quoteChar = '\0';
            }

            if (inQuotes) continue;

            if (c == '(') openBracketCounter++;
            if (c == ')') openBracketCounter--;

            if (openBracketCounter == 0 && input.charAt(i) == delimiter) {
                splitArgsStrings.add(input.substring(lastSplit, i));
                lastSplit = i + 1;
            }
        }

        splitArgsStrings.add(input.substring(lastSplit));

        return splitArgsStrings;
    }

    public static List<String> splitByComma(String input) {
        return splitByChar(input, ',');
    }

    public static List<String> splitBySemicolon(String input) {
        return splitByChar(input, ';');
    }

    public static List<String> splitByCommaAndSemicolon(String input) {
        List<String> splitByComma = splitByComma(input);
        List<String> splitByBoth = splitByComma.stream().map(Parser::splitBySemicolon).flatMap(List::stream).toList();

        return splitByBoth;
    }

    /**
     * function parseProgram which parses the file given in the constructor.
     * It first splits all the lines by '.' outside quote-blocks.
     * Then cleans all the lines.
     * Tries to parse different types in following order:
     * 1. Initialization
     * 2. Dynamic
     * 3. Rule
     * 4. ComplexTerm
     * Each has its own class.
     *
     * @return a populated FactDatabase
     * @throws IOException if an exception occurred during I/O handling
     */
    public FactDatabase parseProgram() throws IOException {
        String dirtyProgram = new String(Files.readAllBytes(Paths.get(path))).trim();
        List<String> lines = Parser.splitByChar(dirtyProgram, '.').stream().map(StringCleaner::cleanString).filter(s -> !s.isEmpty()).toList();

        FactDatabase db = new FactDatabase();

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

        if (verbose) {
            System.out.println("Parsed " + db.size() + " clauses");
            System.out.println(db.getFacts());
            System.out.println("Dynamic statements: " + db.getDynamicsList());
        }

        return db;
    }
}
