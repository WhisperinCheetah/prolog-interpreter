import engine.TermDatabase;
import engine.parser.Parser;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        final String path = "examples/basics/liberty.pl";
        Parser parser = new Parser(path);

        TermDatabase db;
        try {
            db = parser.parseProgram(true);
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            throw new AssertionError("Failed to parse terms from path: " + path);
        }

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
}
