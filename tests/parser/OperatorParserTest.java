package parser;

import interpreter.complex.ComplexTerm;
import interpreter.complex.predicate.Predicate;
import interpreter.complex.predicate.operator.*;
import interpreter.simple.Atom;
import interpreter.simple.Variable;
import org.junit.jupiter.api.Test;
import parser.predicates.OperatorParser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

public class OperatorParserTest {

    @Test
    public void unifyTest() {
        String input = "=(a,b)";

        Optional<Predicate> op = OperatorParser.parse(input);

        assertTrue(op.isPresent());
        assertEquals(new Unify(new ComplexTerm("a"), new ComplexTerm("b")), op.get());
    }

    @Test
    public void notUnifyTest() {
        String input = "\\=(Grade,a)";
        Optional<Predicate> operator = OperatorParser.parse(input);

        assertTrue(operator.isPresent());
        assertEquals(new NotUnify(new Variable("Grade"), new ComplexTerm("a")).toString(), operator.get().toString());
    }

    @Test
    public void equalityTest() {
        String input1 = "==(a,b)";
        String input2 = "==(a,a)";

        Optional<Predicate> op1 = OperatorParser.parse(input1);
        Optional<Predicate> op2 = OperatorParser.parse(input2);
        assertTrue(op1.isPresent());
        assertTrue(op2.isPresent());

        assertEquals(new Equality(new ComplexTerm("a"), new ComplexTerm("b")), op1.get());
        assertEquals(new Equality(new ComplexTerm("a"), new ComplexTerm("a")), op2.get());
    }

    @Test
    public void notEqualityTest() {
        String input1 = "\\==(a,b)";
        String input2 = "\\==(a,a)";

        Optional<Predicate> op1 = OperatorParser.parse(input1);
        Optional<Predicate> op2 = OperatorParser.parse(input2);
        assertTrue(op1.isPresent());
        assertTrue(op2.isPresent());

        assertEquals(new NotEquality(new ComplexTerm("a"), new ComplexTerm("b")), op1.get());
        assertEquals(new NotEquality(new ComplexTerm("a"), new ComplexTerm("a")), op2.get());
    }
}
