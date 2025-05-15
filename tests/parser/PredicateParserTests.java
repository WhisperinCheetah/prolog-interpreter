package parser;

import interpreter.complex.ComplexTerm;
import interpreter.complex.dynamic.*;
import interpreter.complex.expression.Addition;
import interpreter.complex.predicate.*;
import interpreter.complex.predicate.builtins.*;
import interpreter.simple.Atom;
import interpreter.simple.Number;
import interpreter.simple.Variable;
import org.junit.jupiter.api.Test;
import parser.dynamics.DynamicPredicateParser;
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
        String input = "asserta(a)";
        Optional<Dynamic> dyn = DynamicPredicateParser.parse(input);

        assertTrue(dyn.isPresent());
        assertEquals(new Asserta(new Atom("a")).toPrettyString(), dyn.get().toPrettyString());
    }

    @Test
    public void parseAssertTest() {
        String input = "assert(a)";
        Optional<Dynamic> dyn = DynamicPredicateParser.parse(input);

        assertTrue(dyn.isPresent());
        assertEquals(new Assert(new Atom("a")).toPrettyString(), dyn.get().toPrettyString());
    }

    @Test
    public void parseAssertzTest() {
        String input = "assertz(a)";
        Optional<Dynamic> dyn = DynamicPredicateParser.parse(input);

        assertTrue(dyn.isPresent());
        assertEquals(new Assertz(new Atom("a")).toPrettyString(), dyn.get().toPrettyString());
    }

    @Test
    public void parseRetractTest() {
        String input = "retract(a)";
        Optional<Dynamic> dyn = DynamicPredicateParser.parse(input);

        assertTrue(dyn.isPresent());
        assertEquals(new Retract(new Atom("a")).toPrettyString(), dyn.get().toPrettyString());
    }

    @Test
    public void parseRetractallTest() {
        String input = "retractall(a)";
        Optional<Dynamic> dyn = DynamicPredicateParser.parse(input);

        assertTrue(dyn.isPresent());
        assertEquals(new Retractall(new Atom("a")).toPrettyString(), dyn.get().toPrettyString());
    }
}
