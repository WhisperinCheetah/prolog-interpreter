package parser;

import engine.parser.StringCleaner;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CleanStringTests {

    private static final List<List<String>> argPairs = List.of(
            List.of("a", "b"),
            List.of("likes(john,mary)", "X"),
            List.of("'='", "'='"),
            List.of("\"=\"","\"=\""),
            List.of("(=)","(=)")
    );

    private static final List<String> ops = List.of(
            "=",
            "\\=",
            "==",
            "\\=="
    );

    @Test
    public void cleanString() {
        for (String op : ops) {
            for (List<String> argPair : argPairs) {
                String infix = argPair.get(0) + op + argPair.get(1);
                String expectedPrefix = (op.trim() + "(" + argPair.get(0) + "," + argPair.get(1) + ")").replaceAll("\\s+", "");
                String actualPrefix = StringCleaner.cleanString(infix);

                assertEquals(expectedPrefix, actualPrefix);
            }
        }
    }

    @Test
    public void convertToPrefix() {
        for (String op : ops) {
            for (List<String> argPair : argPairs) {
                String infix = argPair.get(0) + op + argPair.get(1);
                String expectedPrefix = op.trim() + "(" + argPair.get(0) + "," + argPair.get(1) + ")";
                String actualPrefix = StringCleaner._convertToPrefix(infix);

                assertEquals(expectedPrefix, actualPrefix);
            }
        }
    }

    @Test
    public void convertToPrefix1() {
        String s = "a=b";
        String asPrefix = StringCleaner._convertToPrefix(s);
        assertEquals("=(a,b)", asPrefix);
    }

    @Test
    public void convertToPrefix2() {
        String s = "likes(john,mary)=X";
        String asPrefix = StringCleaner._convertToPrefix(s);
        assertEquals("=(likes(john,mary),X)", asPrefix);
    }

    @Test
    public void convertToPrefix3() {
        String s = "'='='='";
        String asPrefix = StringCleaner._convertToPrefix(s);
        assertEquals("=('=','=')", asPrefix);
    }

    @Test
    public void convertToPrefix4() {
        String s = "\"=\"=\"=\"";
        String asPrefix = StringCleaner._convertToPrefix(s);
        assertEquals("=(\"=\",\"=\")", asPrefix);
    }

    @Test
    public void convertToPrefix5() {
        String s = "(=)=(=)";
        String asPrefix = StringCleaner._convertToPrefix(s);
        assertEquals("=((=),(=))", asPrefix);
    }

    @Test
    public void convertToPrefix6() {
        String s = "X is Y * 3";
        String asPrefix = StringCleaner._convertToPrefix(s);
        System.out.println(asPrefix);
    }

    public static List<List<String>> isArgPairs = List.of(
            List.of("X", "Y * 3"),
            List.of("X", "Y*3"),
            List.of("John", "Right + 2"),
            List.of("John", "Right - 2"),
            List.of("John", "Right / 2")
    );

    @Test
    public void convertIsToPrefix() {
        final String op = " is ";
        for (List<String> argPair : isArgPairs) {
            String infix = argPair.get(0) + op + argPair.get(1);
            String postfix = StringCleaner.cleanString(infix);
            String expectedPostfix = op.trim() + "(" + argPair.get(0) + "," + StringCleaner.infixToFunctionalPrefix(argPair.get(1)) + ")";

            assertEquals(expectedPostfix, postfix);
        }
    }

    @Test
    public void convertCompoundExpressionsToPrefix() {
        final List<List<String>> expressions = List.of(
                List.of("5 + 3", "+(5,3)"),
                List.of("5 * 3", "*(5,3)"),
                List.of("5 - 3", "-(5,3)"),
                List.of("5 / 3", "/(5,3)"),
                List.of("5 + 3 * 2", "+(5,*(3,2))"),
                List.of("(5 + 3) * 2", "*(+(5,3),2)"),
                List.of("3*2-15*Y", "-(*(3,2),*(15,Y))")
        );

        for (List<String> expressionAndSolution : expressions) {
            String expression = expressionAndSolution.get(0);
            String solution = expressionAndSolution.get(1);

            String actual = StringCleaner.infixToFunctionalPrefix(expression);

            assertEquals(actual, solution);
        }
    }

    @Test
    public void multiSpaceRemoverTest() {
        String s = "a  b  c, d,  e";
        String actual = StringCleaner.removeNonSingleSpaces(s);
        String expected = "a b c, d, e";

        assertEquals(expected, actual);
    }

    @Test
    public void singleSpaceRemoverTest() {
        String s = "a  b  c, d,  e";
        String actual = StringCleaner.removeSingleSpaces(s);
        String expected = "abc,d,e";

        assertEquals(expected, actual);
    }
}
