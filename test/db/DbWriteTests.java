package db;

import engine.Rule;
import engine.Substitution;
import engine.TermDatabase;
import engine.complex.ComplexTerm;
import engine.complex.predicate.Write;
import engine.simple.Atom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DbWriteTests {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @BeforeEach
    void setUpStreams() {
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testIfRedirected() {
        System.out.println("Hello, test!");

        String output = outContent.toString();
        assertTrue(output.contains("Hello, test!"));
    }

    @Test
    public void doesWriteGetWrittenTest() {
        ComplexTerm head = new ComplexTerm("f", List.of());

        Atom sick = new Atom("sick");
        Write bodyTerm = new Write(sick);

        Rule rule = new Rule(head, List.of(bodyTerm));

        TermDatabase db = new TermDatabase(List.of(rule));

        Substitution res = db.backtrack(head);

        System.setOut(originalOut);
        System.out.println("Got output: " + outContent);

        assertTrue(res.isSuccess());
        assertEquals(outContent.toString(), sick.toString());
    }
}
