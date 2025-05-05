import engine.Substitution;
import engine.complex.ComplexTerm;
import engine.simple.Atom;
import engine.simple.Variable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class TestMain {

    @Test
    public void test() {
        Variable A = new Variable("A");
        Variable B = new Variable("B");
        ComplexTerm f1 = new ComplexTerm("f", List.of(A, new Atom("b")));
        ComplexTerm f2 = new ComplexTerm("f", List.of(new Atom("a"), B));

        Substitution sub = f1.unify(f2);
    }

    @Test
    public void test2() {
        Variable A = new Variable("A");
        Atom a = new Atom("a");

        Substitution sub = A.unify(a);

        System.out.println(sub);
    }

}
