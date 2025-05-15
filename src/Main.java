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

    /**
     * The big main function. Creates an object to parse the arguments and adds itself. When it's done
     * it checks if a testing flag was given in the arguments such that the function just gets returned
     * instead of the program being exited.
     *
     * The CommandLine object will call the function 'run'
     */
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

    /**
     * Runs a script
     */
    private void runScript() {
        FactDatabase db = setupDb();
        db.runInitialization();
    }

    /**
     * Starts a REPL
     */
    private void runRepl() throws Exception {
        FactDatabase db = setupDb();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("?- ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            } else {
                db.runQuery(input);
            }
        }
    }

    /**
     * Function run which gets called after the main function. Checks it the program is started
     * in REPL mode or script mode and runs the corresponding function.
     */
    public void _run() throws Exception {
        if (repl) {
            runRepl();
        } else {
            runScript();
        }
    }
}
