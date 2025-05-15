package interpreter.unification;

import interpreter.simple.Atom;
import interpreter.simple.Number;
import interpreter.simple.Variable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleTermUnificationTests {

    @Test
    public void atomUnification1() {
        Atom a = new Atom("a");
        Atom b = new Atom("b");

        assertTrue(a.unify(b).isFailure());
    }

    @Test
    public void atomUnification2() {
        Atom a1 = new Atom("a");
        Atom a2 = new Atom("a");

        assertTrue(a1.unify(a2).isSuccess());
    }

    @Test
    public void numberUnification() {
        Number n = new Number(10.0);
        Number m = new Number(20.0);

        assertTrue(n.unify(m).isFailure());
    }

    @Test
    public void variableUnification() {
        Variable v = new Variable("X");
        Atom a = new Atom("a");

        assertTrue(a.unify(v).isSuccess());
    }
}
