package interpreter.unification;

import interpreter.Rule;
import interpreter.Term;
import interpreter.Unification;
import interpreter.complex.ComplexTerm;
import interpreter.simple.Atom;
import interpreter.simple.Variable;
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

        Unification result = rule.unify(filledHead);

        assertTrue(result.isSuccess());
        assertEquals(result.getMap().get(X), a);
    }
}
