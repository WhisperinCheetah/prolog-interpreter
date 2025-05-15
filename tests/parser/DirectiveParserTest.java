package parser;

import interpreter.FunctorType;
import interpreter.complex.ComplexTerm;
import interpreter.directives.DynamicDirective;
import interpreter.directives.Initialization;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DirectiveParserTest {

    @Test
    public void parseInitializationTest() {
        String input = ":-initialization(main)";
        Optional<Initialization> init = InitializationParser.parse(input);

        assertTrue(init.isPresent());
        assertEquals(new Initialization(new ComplexTerm("main")).toPrettyString(), init.get().toPrettyString());
    }

    @Test
    public void parseDynamicTest() {
        String input = ":-dynamic(likes/2)";
        Optional<DynamicDirective> dyn = DynamicDirectiveParser.parse(input);

        assertTrue(dyn.isPresent());
        assertEquals(new DynamicDirective(new FunctorType("likes", 2)).toPrettyString(), dyn.get().toPrettyString());
    }
}
