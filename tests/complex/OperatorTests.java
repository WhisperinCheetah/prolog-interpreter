package complex;

import interpreter.Term;
import interpreter.complex.ComplexTerm;
import interpreter.complex.predicate.operator.Equality;
import interpreter.complex.predicate.operator.NotEquality;
import interpreter.simple.Atom;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OperatorTests {

    @Test
    public void equalityTest1() {
        Term a = new Atom("hello");
        Term b = new Atom("hello");
        Term c = new Atom("notHello");

        assertTrue(new Equality(a, b).execute().isSuccess());
        assertTrue(new Equality(b, a).execute().isSuccess());
        assertTrue(new Equality(a, c).execute().isFailure());
    }

    @Test
    public void equalityTest2() {
        Term a = new ComplexTerm("f", List.of(new Atom("hello"), new Atom("world")));
        Term b = new ComplexTerm("f", List.of(new Atom("hello"), new Atom("world")));
        Term c = new ComplexTerm("f", List.of(new Atom("world"), new Atom("hello")));

        assertTrue(new Equality(a, b).execute().isSuccess());
        assertTrue(new Equality(b, a).execute().isSuccess());
        assertTrue(new Equality(a, c).execute().isFailure());
    }

    @Test
    public void inequalityTest1() {
        Term a = new ComplexTerm("f", List.of(new Atom("hello"), new Atom("world")));
        Term b = new ComplexTerm("f", List.of(new Atom("hello"), new Atom("world")));
        Term c = new ComplexTerm("f", List.of(new Atom("world"), new Atom("hello")));

        assertTrue(new NotEquality(a, b).execute().isFailure());
        assertTrue(new NotEquality(b, a).execute().isFailure());
        assertTrue(new NotEquality(a, c).execute().isSuccess());
    }
}
