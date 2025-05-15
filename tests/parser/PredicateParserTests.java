package parser;

import interpreter.complex.ComplexTerm;
import interpreter.complex.expression.Addition;
import interpreter.complex.predicate.*;
import interpreter.simple.Atom;
import interpreter.simple.Number;
import interpreter.simple.Variable;
import org.junit.jupiter.api.Test;
import parser.predicates.PredicateParser;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PredicateParserTests {

    @Test
    public void parseWriteTest() {
        String input = "write(f(a))";
        Optional<Predicate> predicate = PredicateParser.parse(input);

        assertTrue(predicate.isPresent());
        assertEquals(new Write(new ComplexTerm("f", List.of(new Atom("a")))), predicate.get());
    }

    @Test
    public void parseReadTest() {
        String input = "read(f(a))";
        Optional<Predicate> predicate = PredicateParser.parse(input);

        assertTrue(predicate.isPresent());
        assertEquals(new Read(new ComplexTerm("f", List.of(new Atom("a")))), predicate.get());
    }

    @Test
    public void parseNLTest() {
        String input = "nl";
        Optional<Predicate> predicate = PredicateParser.parse(input);

        assertTrue(predicate.isPresent());
        assertEquals(new NL(), predicate.get());
    }

    @Test
    public void parseFailTest() {
        String input = "fail";
        Optional<Predicate> predicate = PredicateParser.parse(input);

        assertTrue(predicate.isPresent());
        assertEquals(new Fail(), predicate.get());
    }

    @Test
    public void parseIsTest() {
        String input = "is(5,+(3,2))";
        Optional<Predicate> predicate = PredicateParser.parse(input);

        assertTrue(predicate.isPresent());
        assertEquals(new Is(new Number(5), new Addition(new Number(3), new Number(2))).toPrettyString(), predicate.get().toPrettyString());
    }

    @Test
    public void parseSuccTest1() {
        String input = "succ(1,2)";
        Optional<Predicate> predicate = PredicateParser.parse(input);

        assertTrue(predicate.isPresent());
        assertEquals(new Succ(new Number(1), new Number(2)).toPrettyString(), predicate.get().toPrettyString());
    }

    @Test
    public void parseSuccTest2() {
        String input = "succ(1,X)";
        Optional<Predicate> predicate = PredicateParser.parse(input);

        assertTrue(predicate.isPresent());
        assertEquals(new Succ(new Number(1), new Variable("X")).toPrettyString(), predicate.get().toPrettyString());
    }

    @Test
    public void parseBetweenTest() {
        String input = "between(2,5,3)";
        Optional<Predicate> predicate = PredicateParser.parse(input);

        assertTrue(predicate.isPresent());
        assertEquals(new Between(new Number(2), new Number(5), new Number(3)).toPrettyString(), predicate.get().toPrettyString());
    }

    @Test
    public void parseCut() {
        String input = "!";
        Optional<Predicate> predicate = PredicateParser.parse(input);

        assertTrue(predicate.isPresent());
        assertEquals(new Cut(), predicate.get());
    }

    @Test
    public void parseAssertaTest() {

    }

    @Test
    public void parseAssertTest() {

    }

    @Test
    public void parseAssertzTest() {

    }

    @Test
    public void parseRetractTest() {

    }

    @Test
    public void parseRetractallTest() {

    }
}
