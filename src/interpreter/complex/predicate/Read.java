package interpreter.complex.predicate;

import interpreter.Substitution;
import interpreter.Term;
import parser.TermParser;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Read extends Predicate {
    public Read(Term arg) {
        super("read", List.of(arg), 1);
    }

    public static boolean isRead(String input) {
        return input.matches("^read\\(.*\\)");
    }

    @Override
    public Substitution execute() {
        Scanner scanner = new Scanner(System.in); // create a scanner
        System.out.print("Enter a prolog term: ");
        String input = scanner.nextLine(); // read a line of text

        Optional<Term> maybeTerm = TermParser.parse(input);

        if (maybeTerm.isEmpty()) {
            throw new RuntimeException("Could not parse " + input);
        }

        Term term = maybeTerm.get();

        return term.unify(this.getArg(0));
    }

    @Override
    public Read substituteVariables(Substitution substitution) {
        Term filledArg = this.getArg(0).substituteVariables(substitution);

        return new Read(filledArg);
    }

    @Override
    public Read copy() {
        return new Read(this.getArg(0).copy());
    }
}
