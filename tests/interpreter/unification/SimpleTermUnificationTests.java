package interpreter.unification;

import interpreter.Rule;
import interpreter.complex.ComplexTerm;
import interpreter.simple.Atom;
import interpreter.simple.Number;
import interpreter.simple.Variable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleTermUnificationTests {

    @Test
    public void atomUnification() {
        Atom a = new Atom("a");
        Atom a1 = new Atom("a");
        Atom b = new Atom("b");
        Number c = new Number(10.0);

        assertTrue(a1.unify(a).isSuccess());
        assertTrue(b.unify(a).isFailure());
        assertTrue(c.unify(a).isFailure());
    }

    @Test
    public void numberUnification() {
        Number n = new Number(10.0);
        Number n1 = new Number(10.0);
        Number m = new Number(20.0);
        Atom a = new Atom("a");

        assertTrue(m.unify(n).isFailure());
        assertTrue(a.unify(n).isFailure());
        assertTrue(n1.unify(n).isSuccess());
    }

    @Test
    public void variableUnification() {
        Variable v = new Variable("X");
        Atom a = new Atom("a");
        Number b = new Number(10.0);
        ComplexTerm c = new ComplexTerm("f");
        Rule d = new Rule(c, List.of(c));

        assertTrue(a.unify(v).isSuccess());
        assertTrue(b.unify(v).isSuccess());
        assertTrue(c.unify(v).isSuccess());
        assertTrue(d.unify(v).isSuccess());
    }
}
