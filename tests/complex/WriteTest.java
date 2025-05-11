package complex;

import interpreter.complex.predicate.Write;
import interpreter.simple.Atom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class WriteTest {

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
    public void writeTest() {
        Write write = new Write(new Atom("hello"));

        write.execute();

        assertTrue(outContent.toString().contains("hello"));
    }
}
