package parser;

import engine.parser.Parser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConvertToPrefixTests {

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
    public void convertToPrefix() {
        for (String op : ops) {
            for (List<String> argPair : argPairs) {
                String infix = argPair.get(0) + op + argPair.get(1);
                String expectedPrefix = op + "(" + argPair.get(0) + "," + argPair.get(1) + ")";
                String actualPrefix = Parser.convertToPrefix(infix);

                assertEquals(expectedPrefix, actualPrefix);
            }
        }
    }

    @Test
    public void convertToPrefix1() {
        String s = "a=b";
        String asPrefix = Parser.convertToPrefix(s);
        assertEquals("=(a,b)", asPrefix);
    }

    @Test
    public void convertToPrefix2() {
        String s = "likes(john,mary)=X";
        String asPrefix = Parser.convertToPrefix(s);
        assertEquals("=(likes(john,mary),X)", asPrefix);
    }

    @Test
    public void convertToPrefix3() {
        String s = "'='='='";
        String asPrefix = Parser.convertToPrefix(s);
        assertEquals("=('=','=')", asPrefix);
    }

    @Test
    public void convertToPrefix4() {
        String s = "\"=\"=\"=\"";
        String asPrefix = Parser.convertToPrefix(s);
        assertEquals("=(\"=\",\"=\")", asPrefix);
    }

    @Test
    public void convertToPrefix5() {
        String s = "(=)=(=)";
        String asPrefix = Parser.convertToPrefix(s);
        assertEquals("=((=),(=))", asPrefix);
    }
}
