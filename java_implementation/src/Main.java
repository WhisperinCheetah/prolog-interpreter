package src;

import src.parser.Parser;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        final String path = "examples/basics/write.pl";
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
            } else {
                db.runQuery(input);
            }
        }
    }
}
