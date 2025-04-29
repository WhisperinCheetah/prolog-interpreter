package src.clauses;

import src.Structure;
import src.parser.Parser;
import src.Term;
import src.TermDatabase;
import src.parser.StructureParser;
import src.simples.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Rule extends Clause {
    Fact head;
    List<Structure> body;

    public Rule(Fact head, List<Structure> body) {
        super(head.getFunctorType());
        this.head = head;
        this.body = body;
    }

    public Rule(Rule other) {
        super(other.head.getFunctorType());
        this.head = other.head.copy();
        this.body = new ArrayList<>(other.body.size());
        other.body.forEach(s -> this.body.add(s.copy()));
    }

    public Rule copy() {
        return new Rule(this);
    }

    public static boolean isRule(String line) {
        return line.matches("[a-z]+[a-zA-Z]*:-([a-z]+[a-zA-Z]*\\([^\\-:]*\\),?)+");
        // return line.matches("[a-z]+[a-zA-Z]*\\([^\\-:]*\\):-([a-z]+[a-zA-Z]*\\([^\\-:]*\\),?)+");
    }

    public Fact getHead() {
        return head;
    }

    public List<Structure> getBody() {
        return body;
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

        List<Structure> body = bodyParts.stream().map(StructureParser::parseStructure)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return new Rule(head, body);
    }

    @Override
    public boolean resolve(TermDatabase db, Fact query) {
        if (!this.head.shallowEquals(query)) {
            return false;
        }

        Rule copy = new Rule(this);
        copy.fill(query);

        return copy.body.stream().allMatch(db::resolveQuery);
    }

    @Override
    public void fillVariable(Variable var, Term fill) {
        this.head.fillVariable(var, fill);

        for (Structure fact : body) {
            fact.fillVariable(var, fill);
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
