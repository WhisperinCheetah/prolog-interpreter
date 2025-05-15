import interpreter.FactDatabase;
import parser.Parser;

import java.io.IOException;
import java.util.Scanner;

import picocli.CommandLine;
import picocli.CommandLine.Option;


/**
 * Class main that parses arguments and starts a REPL loop or script mode.
 */
public class Main implements Runnable {

    @Option(names = "-s", description = "Input file")
    String inputFile;

    @Option(names = {"-r", "--repl"}, description = "Start REPL")
    boolean repl;

    @Option(names = {"-v", "--verbose"}, description="Enable verbosity")
    boolean verbose;

    @Option(names = {"-t", "--testing"}, description="Disable System.exit")
    boolean testing;

    public static void main(String[] args) throws Exception {
        int exitCode = new CommandLine(new Main()).execute(args);

        for (String arg : args) {
            if (arg.equals("-t") || arg.equals("--testing")) {
                return;
            }
        }

        System.exit(exitCode);
    }

    @Override
    public void run() {
        try {
            _run();
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    /**
     * @return FactDatabase from a file if inputFile != null, otherwise empty
     */
    private FactDatabase setupDb() {
        if (inputFile == null) {
            return new FactDatabase();
        }

        Parser parser = new Parser(inputFile, verbose);

        try {
            return parser.parseProgram();
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            throw new RuntimeException("Failed to parse terms from path: " + inputFile);
        }
    }

    private void runScript() {
        FactDatabase db = setupDb();
        db.runInitialization();
    }

    private void runRepl() throws Exception {
        FactDatabase db = setupDb();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            } else if (input.equalsIgnoreCase("")) {
                System.out.print("\r");
                db.nextState();
            } else if (input.startsWith("?-")) {
                db.runQuery(input.substring(input.indexOf("?-") + 2));
            }
        }
    }

    public void _run() throws Exception {
        if (repl) {
            runRepl();
        } else {
            runScript();
        }
    }
}
