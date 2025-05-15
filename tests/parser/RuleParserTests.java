package parser;

import interpreter.Rule;
import interpreter.complex.ComplexTerm;
import interpreter.simple.Atom;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RuleParserTests {

    @Test
    public void parseRule1() {
        String input = "f(a):-g(a)";
        Optional<Rule> rule = RuleParser.parse(input);

        assertTrue(rule.isPresent());

        ComplexTerm head = new ComplexTerm("f", List.of(new Atom("a")));
        ComplexTerm body = new ComplexTerm("g", List.of(new Atom("a")));
        assertEquals(new Rule(head, List.of(body)).toPrettyString(), rule.get().toPrettyString());
    }

    @Test
    public void parseRule2() {
        String input = "f(a):-g(a),h(a)";
        Optional<Rule> rule = RuleParser.parse(input);

        assertTrue(rule.isPresent());

        ComplexTerm head = new ComplexTerm("f", List.of(new Atom("a")));
        ComplexTerm body1 = new ComplexTerm("g", List.of(new Atom("a")));
        ComplexTerm body2 = new ComplexTerm("h", List.of(new Atom("a")));
        assertEquals(new Rule(head, List.of(body1, body2)).toPrettyString(), rule.get().toPrettyString());
    }
}
