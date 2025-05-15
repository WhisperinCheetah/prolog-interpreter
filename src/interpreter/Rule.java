package interpreter;

import interpreter.complex.ComplexTerm;
import interpreter.simple.Variable;

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

    /**
     * Only unifies the head. The body needs to be checked in the database itself.
     *
     * @param other The Term to get unified with
     * @return a Unification
     */
    @Override
    public Unification unify(Term other) {
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
    public Rule substituteVariables(Unification unification) {
        ComplexTerm head = this.head.substituteVariables(unification);
        List<Term> body = this.body.stream().map(t -> t.substituteVariables(unification)).toList();

        return new Rule(head, body);
    }

    public Rule copy() {
        return new Rule(this);
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

    @Override
    public String toString() {
        return head.toString() + " :- " + body.toString();
    }

    public String toPrettyString() {
        return head.toPrettyString() + " :- " + body.toString();
    }
}
