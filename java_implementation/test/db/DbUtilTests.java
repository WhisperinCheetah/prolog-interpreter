package db;

import engine.Term;
import engine.TermDatabase;
import engine.complex.ComplexTerm;
import engine.simple.Atom;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.util.List;

public class DbUtilTests {

    @Test
    public void parseQueryTest() {
        TermDatabase db = new TermDatabase();
        String query = "likes(john,mary)";

        Term parsedQuery = null;
        try {
            parsedQuery = db.parseQuery(query);
        } catch (ParseException e) {
            fail("Failed to parse query: " + e.getMessage());
        }

        assertEquals(parsedQuery, new ComplexTerm("likes", List.of(new Atom("john"), new Atom("mary"))));
    }
}
