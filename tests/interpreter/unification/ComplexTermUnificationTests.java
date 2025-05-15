package interpreter.unification;

import interpreter.Unification;
import interpreter.complex.ComplexTerm;
import interpreter.simple.Atom;
import interpreter.simple.Variable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ComplexTermUnificationTests {

    @Test
    public void complexTermUnification1() {
        Variable A = new Variable("A");
        Variable B = new Variable("B");

        Atom a = new Atom("a");
        Atom b = new Atom("b");

        ComplexTerm f1 = new ComplexTerm("f", List.of(A, b));
        ComplexTerm f2 = new ComplexTerm("f", List.of(a, B));

        Unification result = f1.unify(f2);

        assertTrue(result.isSuccess());
        assertEquals(result.getMap().get(A), a);
        assertEquals(result.getMap().get(B), b);
    }

    @Test
    public void complexTermUnification2() {
        Variable A = new Variable("A");
        ComplexTerm f = new ComplexTerm("f", List.of());

        Unification result = f.unify(A);

        assertTrue(result.isSuccess());
        assertEquals(result.getMap().get(A), f);
    }

    @Test
    public void complexTermUnification3() {
        Atom a = new Atom("a");
        ComplexTerm f = new ComplexTerm("f", List.of());

        Unification result = f.unify(a);

        assertTrue(result.isFailure());
    }

    @Test
    public void complexTermUnification4() {
        ComplexTerm f1 = new ComplexTerm("f1", List.of());
        ComplexTerm f2 = new ComplexTerm("f2", List.of(f1));

        Variable A = new Variable("A");
        ComplexTerm f2WithVar = new ComplexTerm("f2", List.of(A));

        Unification result = f2.unify(f2WithVar);

        assertTrue(result.isSuccess());
        assertEquals(result.getMap().get(A), f1);
    }
}
