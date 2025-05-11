package db;

import interpreter.Term;
import interpreter.FactDatabase;
import interpreter.complex.ComplexTerm;
import interpreter.simple.Atom;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.util.List;

public class DbUtilTests {

    @Test
    public void parseQueryTest() {
        FactDatabase db = new FactDatabase();
        String query = "likes(john,mary)";

        List<Term> parsedQuery = null;
        try {
            parsedQuery = db.parseQuery(query);
        } catch (ParseException e) {
            fail("Failed to parse query: " + e.getMessage());
        }

        assertEquals(1, parsedQuery.size());
        assertEquals(parsedQuery.getFirst(), new ComplexTerm("likes", List.of(new Atom("john"), new Atom("mary"))));
    }
}
