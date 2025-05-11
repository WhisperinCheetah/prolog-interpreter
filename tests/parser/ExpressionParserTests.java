package parser;

import interpreter.complex.expression.Addition;
import interpreter.complex.expression.EvaluableExpression;
import interpreter.complex.expression.Multiplication;
import interpreter.complex.expression.Subtraction;
import interpreter.simple.Number;
import interpreter.simple.Variable;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ExpressionParserTests {

    @Test
    public void parseExpression1() {
        String dirtyExpression = "1 + 2";
        String cleanExpression = StringCleaner.infixToFunctionalPrefix(dirtyExpression);

        Optional<EvaluableExpression> e = ExpressionParser.parse(cleanExpression);

        assertTrue(e.isPresent());
        assertEquals(e.get().toString(), new Addition(new Number(1.0), new Number(2.0)).toString());
    }

    @Test
    public void parseExpression2() {
        String dirtyExpression = "X + 3 * 2 - 15 * Y";
        String cleanExpression = StringCleaner.infixToFunctionalPrefix(dirtyExpression);

        Optional<EvaluableExpression> e = ExpressionParser.parse(cleanExpression);
        EvaluableExpression wanted = new Addition(new Variable("X"), new Subtraction(
                new Multiplication(new Number(3.0), new Number(2.0)),
                new Multiplication(new Number(15.0), new Variable("Y")))
        );

        assertTrue(e.isPresent());
        assertEquals(wanted.toString(), e.get().toString());
    }

    @Test
    public void parseExpression3() {
        String dirtyExpression = "(X - Y - Y - Y - Y)";
        String cleanExpression = StringCleaner.infixToFunctionalPrefix(dirtyExpression);

        Optional<EvaluableExpression> e = ExpressionParser.parse(cleanExpression);
        EvaluableExpression wanted = new Subtraction(new Variable("X"), new Subtraction(new Variable("Y"), new Subtraction(new Variable("Y"), new Subtraction(new Variable("Y"), new Variable("Y")))));

        assertTrue(e.isPresent());
        assertEquals(wanted.toString(), e.get().toString());
    }
}
