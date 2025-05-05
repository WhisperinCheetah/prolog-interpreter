package complex;

import engine.Substitution;
import engine.complex.ComplexTerm;
import engine.simple.Atom;
import engine.simple.Variable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ComplexTermUnificationTests {

    @Test
    public void complexTermUnification() {
        Variable A = new Variable("A");
        Variable B = new Variable("B");

        Atom a = new Atom("a");
        Atom b = new Atom("b");

        ComplexTerm f1 = new ComplexTerm("f", List.of(A, b));
        ComplexTerm f2 = new ComplexTerm("f", List.of(a, B));

        Substitution result = f1.unify(f2);

        assertTrue(result.isSuccess());
        assertEquals(result.getMap().get(A), a);
        assertEquals(result.getMap().get(B), b);
    }
}
