import interpreter.Unification;
import interpreter.complex.ComplexTerm;
import interpreter.simple.Atom;
import interpreter.simple.Variable;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestMain {

    @Test
    public void test() {
        Variable A = new Variable("A");
        Variable B = new Variable("B");
        ComplexTerm f1 = new ComplexTerm("f", List.of(A, new Atom("b")));
        ComplexTerm f2 = new ComplexTerm("f", List.of(new Atom("a"), B));

        Unification sub = f1.unify(f2);
    }

    @Test
    public void test2() {
        Variable A = new Variable("A");
        Atom a = new Atom("a");

        Unification sub = A.unify(a);

        System.out.println(sub);
    }

}
