import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Rule implements Term {
    Fact head;
    List<Term> body;

    public Rule(Fact head, List<Term> body) {
        this.head = head;
        this.body = body;
    }

    public Rule(Rule other) {
        this.head = new Fact(other.head);
        this.body = new ArrayList<>(other.body);
    }

    public static boolean isRule(String line) {
        return line.matches("[a-z]+[a-zA-Z]*\\([^\\-:]*\\):-([a-z]+[a-zA-Z]*\\([^\\-:]*\\),?)+");
    }

    private static ArrayList<String> splitBodyString(String body) {
        ArrayList<String> bodyParts = new ArrayList<>();

        int start = 0;
        for (int i = 0; i < body.length(); i++) {
            if (body.charAt(i) == '(') {
                i = body.indexOf(')', i);
            }

            if (body.charAt(i) == ',') {
                bodyParts.add(body.substring(start, i));
                start = i + 1;
            }
        }

        bodyParts.add(body.substring(start));

        return bodyParts;
    }

    public static Rule fromString(String line) {
        String[] parts = line.split(":-");

        Fact head = Fact.fromString(parts[0]);
        ArrayList<String> bodyParts = splitBodyString(parts[1]);

        List<Term> body = bodyParts.stream().map(Parser::parseChunk).toList();

        return new Rule(head, body);
    }

    public boolean resolve(TermDatabase db, Fact query) {
        if (this.head.shallowEquals(query)) {
            Rule copy = new Rule(this);
            copy.fill(query);

            return copy.body.stream().allMatch(t -> t.resolve(db, query));
        }
    }

    public void fillVariable(Variable var, Term fill) {
        this.head.fillVariable(var, fill);

        for (Term term : body) {
            term.fillVariable(var, fill);
        }
    }

    // TODO: recursive for fact
    public void fill(Fact fills) {
        Fact head = this.head;
        for (int i = 0; i < this.head.args.size(); i++) {
            if (head.args.get(i) instanceof Variable var) {
                Term fill = fills.args.get(i);
                this.fillVariable(var, fill);
            }
        }
    }

    @Override
    public String toString() {
        return head.toString() + " :- " + body.toString();
    }
}
