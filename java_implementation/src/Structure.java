package src;

import src.complex.ComplexTerm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Structure implements Fact {
    ComplexTerm head;
    List<Fact> body;

    public Structure(ComplexTerm head, List<Fact> body) {
        this.head = head;
        this.body = body;
    }

    public Structure(Structure other) {
        this.head = other.head.copy();
        this.body = other.body.stream().map(Fact::copy).collect(Collectors.toList());
    }

    public Structure copy() {
        return new Structure(this);
    }

    @Override
    public TermType getTermType() {
        return TermType.STRUCTURE;
    }

    public static boolean isRule(String line) {
        return line.matches("[a-z]+[a-zA-Z]*\\([^\\-:]*\\):-([a-z]+[a-zA-Z]*\\([^\\-:]*\\),?)+");
    }

    public ComplexTerm getHead() {
        return head;
    }

    public List<Fact> getBody() {
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
