package engine;

import engine.complex.ComplexTerm;
import engine.simple.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Rule implements Fact {
    public static String RULE_REGEX = ComplexTerm.COMPLEX_TERM_REGEX + ":-.+";

    ComplexTerm head;
    List<Term> body;

    public Rule(ComplexTerm head, List<Term> body) {
        this.head = head;
        this.body = body;
    }

    public Rule(Rule other) {
        this.head = other.head.copy();
        this.body = other.body.stream().map(Term::copy).collect(Collectors.toList());
    }

    @Override
    public Substitution unify(Term other) {
        return head.unify(other);
    }

    @Override
    public Fact renameVariables(HashMap<String, Variable> map) {
        Rule copy = this.copy();

        copy.head = copy.head.renameVariables(map);
        copy.body = copy.body.stream().map(t -> t.renameVariables(map)).toList();

        return copy;
    }

    @Override
    public Rule substituteVariables(Substitution substitution) {
        ComplexTerm head = this.head.substituteVariables(substitution);
        List<Term> body = this.body.stream().map(t -> t.substituteVariables(substitution)).toList();

        return new Rule(head, body);
    }

    public Rule copy() {
        return new Rule(this);
    }

    @Override
    public TermType getTermType() {
        return TermType.STRUCTURE;
    }

    public static boolean isRule(String line) {
        return line.replaceAll("\\s+", "").matches(RULE_REGEX);
    }

    public ComplexTerm getHead() {
        return head;
    }

    public List<Term> getBody() {
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

    @Override
    public String toString() {
        return head.toString() + " :- " + body.toString();
    }
}
