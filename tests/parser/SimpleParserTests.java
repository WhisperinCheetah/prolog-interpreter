package parser;

import parser.simples.AtomParser;
import parser.simples.NumberParser;
import parser.simples.SimpleTermParser;
import parser.simples.VariableParser;
import interpreter.simple.Atom;
import interpreter.simple.Number;
import interpreter.simple.SimpleTerm;
import interpreter.simple.Variable;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleParserTests {

    @Test
    public void parseNumberTest1() {
        String input = "19.2";
        Optional<Number> parsed = NumberParser.parse(input);

        assertTrue(parsed.isPresent());
        assertEquals(19.2, parsed.get().getValue());
    }

    @Test
    public void parseNumberTest2() {
        String input = "1";
        Optional<Number> parsed = NumberParser.parse(input);

        assertTrue(parsed.isPresent());
        assertEquals(1, parsed.get().getValue());
    }

    @Test
    public void parseAtomTest1() {
        String input = "monkeyMan";
        Optional<Atom> parsed = AtomParser.parse(input);

        assertTrue(parsed.isPresent());
        assertEquals(input, parsed.get().getValue());
    }

    @Test
    public void parseAtomTest2() {
        String input = "'Monkey loves banana'";
        Optional<Atom> parsed = AtomParser.parse(input);

        assertTrue(parsed.isPresent());
        assertEquals("Monkey loves banana", parsed.get().getValue());
    }

    @Test
    public void parseVariableTest1() {
        String input = "XFooBar";
        Optional<Variable> parsed = VariableParser.parse(input);

        assertTrue(parsed.isPresent());
        assertEquals(input, parsed.get().getName());
    }

    @Test
    public void parseVariableTest2() {
        String input = "_Man";
        Optional<Variable> parsed = VariableParser.parse(input);

        assertTrue(parsed.isPresent());
        assertEquals(input, parsed.get().getName());
    }

    @Test
    public void parseSimpleTermTest1() {
        String input = "gragraSki";
        Optional<SimpleTerm> maybeParsed = SimpleTermParser.parse(input);

        assertTrue(maybeParsed.isPresent());

        SimpleTerm parsed = maybeParsed.get();
        assertInstanceOf(Atom.class, parsed);
        assertEquals(input, ((Atom)parsed).getValue());
    }

    @Test
    public void parseSimpleTermTest2() {
        String input = "likes(mary)";
        Optional<SimpleTerm> maybeParsed = SimpleTermParser.parse(input);

        assertTrue(maybeParsed.isEmpty());
    }
}
