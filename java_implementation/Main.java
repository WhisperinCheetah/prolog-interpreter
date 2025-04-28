import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        final String path = "main.pl";
        Parser parser = new Parser(path);

        List<Term> terms;
        try {
            terms = parser.parse();
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            throw new AssertionError("Failed to parse terms from path: " + path);
        }

        System.out.println("Parsing complete");
        System.out.println("Parsed " + terms.size() + " terms");

        TermDatabase db = new TermDatabase(terms);
        System.out.println(db);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            } else if (input.equalsIgnoreCase("")) {
                db.nextState();
            } else {
                db.runQuery(input);
            }
        }
    }
}
