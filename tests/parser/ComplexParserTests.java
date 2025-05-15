package parser;

import interpreter.complex.ComplexTerm;
import interpreter.simple.Atom;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ComplexParserTests {

    @Test
    public void parseComplex1() {
        String input = "a";
        Optional<ComplexTerm> res = ComplexTermParser.parse(input);

        assertTrue(res.isPresent());
        assertEquals(new ComplexTerm("a"), res.get());
    }

    @Test
    public void parseComplex2() {
        String input = "f(a)";
        Optional<ComplexTerm> res = ComplexTermParser.parse(input);

        assertTrue(res.isPresent());
        assertEquals(new ComplexTerm("f", List.of(new Atom("a"))), res.get());
    }

    @Test
    public void parseComplex3() {
        String input = "f(a,b)";
        Optional<ComplexTerm> res = ComplexTermParser.parse(input);

        assertTrue(res.isPresent());
        assertEquals(new ComplexTerm("f", List.of(new Atom("a"), new Atom("b"))), res.get());
    }

    @Test
    public void parseNestedComplex1() {
        String input = "f(f(a))";
        Optional<ComplexTerm> res = ComplexTermParser.parse(input);

        assertTrue(res.isPresent());
        assertEquals(new ComplexTerm("f", List.of(new ComplexTerm("f", List.of(new Atom("a"))))), res.get());
    }

    @Test
    public void parseNestedComplex2() {
        String input = "f(f(a),g(b))";
        Optional<ComplexTerm> res = ComplexTermParser.parse(input);

        assertTrue(res.isPresent());
        assertEquals(new ComplexTerm("f", List.of(new ComplexTerm("f", List.of(new Atom("a"))), new ComplexTerm("g", List.of(new Atom("b"))))), res.get());
    }

    @Test
    public void parseNestedComplex3() {
        String input = "f(a,g(h(b)))";
        Optional<ComplexTerm> res = ComplexTermParser.parse(input);

        assertTrue(res.isPresent());
        assertEquals(new ComplexTerm("f", List.of(new Atom("a"), new ComplexTerm("g", List.of(new ComplexTerm("h", List.of(new Atom("b"))))))), res.get());
    }

    @Test
    public void parseNotComplex1() {
        String input = "X";
        Optional<ComplexTerm> res = ComplexTermParser.parse(input);

        assertTrue(res.isEmpty());
    }

    @Test
    public void parseNotComplex2() {
        String input = "f(X) :- g(X)";
        Optional<ComplexTerm> res = ComplexTermParser.parse(input);

        assertTrue(res.isEmpty());
    }

    @Test
    public void parseNoArgsComplex() {
        String input = "f";
        Optional<ComplexTerm> res = ComplexTermParser.parse(input);

        assertTrue(res.isPresent());
        assertEquals(new ComplexTerm("f"), res.get());
    }
}
