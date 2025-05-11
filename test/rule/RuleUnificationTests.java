package rule;

import engine.Rule;
import engine.Substitution;
import engine.Term;
import engine.complex.ComplexTerm;
import engine.simple.Atom;
import engine.simple.Variable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class RuleUnificationTests {

    @Test
    public void ruleUnification() {
        Variable X = new Variable("X");
        ComplexTerm head = new ComplexTerm("f", List.of(X));

        List<Term> body = List.of(
                new ComplexTerm("g", List.of(X))
        );

        Rule rule = new Rule(head, body);

        Atom a = new Atom("a");
        ComplexTerm filledHead = new ComplexTerm("f", List.of(a));

        Substitution result = rule.unify(filledHead);

        assertTrue(result.isSuccess());
        assertEquals(result.getMap().get(X), a);
    }
}
