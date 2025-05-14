package parser;

import interpreter.complex.ComplexTerm;
import interpreter.complex.predicate.Predicate;
import interpreter.complex.predicate.operator.NotUnify;
import interpreter.complex.predicate.operator.Operator;
import interpreter.simple.Atom;
import interpreter.simple.Variable;
import org.junit.jupiter.api.Test;
import parser.predicates.OperatorParser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class OperatorParserTest {

    @Test
    public void notUnifyTest() {
        String input = "\\=(Grade,a)";
        Optional<Predicate> operator = OperatorParser.parse(input);

        assertTrue(operator.isPresent());
        assertEquals(new NotUnify(new Variable("Grade"), new ComplexTerm("a")).toString(), operator.get().toString());
    }
}
