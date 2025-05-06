package db;

import engine.Fact;
import engine.Rule;
import engine.Substitution;
import engine.TermDatabase;
import engine.complex.ComplexTerm;
import engine.complex.Write;
import engine.simple.Atom;
import engine.simple.Variable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class DbBacktrackingTests {

    public TermDatabase getTermDatabase() {
        ComplexTerm dbComplex = new ComplexTerm("likes", List.of(new Atom("john"), new Atom("mary")));

        TermDatabase db = new TermDatabase();
        db.addFact(dbComplex);
        db.finalizeDatabase();

        return db;
    }

    @Test
    public void backtrackingComplexTermTest1() {
        TermDatabase db = getTermDatabase();

        ComplexTerm query = new ComplexTerm("likes", List.of(new Atom("john"), new Atom("mary")));
        Substitution result = db.backtrack(query);

        assertTrue(result.isSuccess());
    }

    @Test
    public void backtrackingComplexTermTest2() {
        TermDatabase db = getTermDatabase();

        ComplexTerm query1 = new ComplexTerm("likes", List.of(new Atom("john"), new Atom("john")));
        Atom query2 = new Atom("john");

        Substitution result1 = db.backtrack(query1);
        Substitution result2 = db.backtrack(query2);

        assertTrue(result1.isFailure());
        assertTrue(result2.isFailure());
    }

    @Test
    public void backtrackingComplexTermTest3() {
        TermDatabase db = getTermDatabase();

        Variable X = new Variable("X");
        ComplexTerm query = new ComplexTerm("likes", List.of(new Atom("john"), X));

        Substitution result = db.backtrack(query);

        assertTrue(result.isSuccess());
        assertEquals(new Atom("mary"), result.getMap().get(X));
    }

    public TermDatabase getTermDatabaseWithVars() {
        ComplexTerm dbComplex = new ComplexTerm("likes", List.of(new Atom("john"), new Variable("X")));

        TermDatabase db = new TermDatabase();
        db.addFact(dbComplex);
        db.finalizeDatabase();

        return db;
    }

    @Test
    public void backtrackingComplexTermTest4() {
        TermDatabase db = getTermDatabaseWithVars();

        ComplexTerm query = new ComplexTerm("likes", List.of(new Atom("john"), new Atom("mary")));

        Substitution result = db.backtrack(query);

        assertTrue(result.isSuccess());
    }

    @Test
    public void backtrackingComplexTermTest5() {
        TermDatabase db = getTermDatabaseWithVars();

        Variable X = new Variable("X");
        ComplexTerm query = new ComplexTerm("likes", List.of(X, new Atom("mary")));

        Substitution result = db.backtrack(query);

        assertTrue(result.isSuccess());
        assertEquals(new Atom("john"), result.getMap().get(X));
    }

    public TermDatabase getTermDatabaseWithRule(boolean misleading) {
        ComplexTerm head = new ComplexTerm("loves", List.of(new Variable("X"), new Variable("Y")));
        ComplexTerm body1 = new ComplexTerm("likes", List.of(new Variable("X"), new Variable("Y")));
        ComplexTerm body2 = new ComplexTerm("likes", List.of(new Variable("Y"), new Variable("X")));
        Rule rule = new Rule(head, List.of(body1, body2));

        ComplexTerm misleadingTerm = new ComplexTerm("likes", List.of(new Atom("john"), new Atom("lee")));
        ComplexTerm f1 = new ComplexTerm("likes", List.of(new Atom("john"), new Atom("mary")));
        ComplexTerm f2 = new ComplexTerm("likes", List.of(new Atom("mary"), new Atom("john")));

        List<Fact> facts = new ArrayList<>();

        if (misleading) facts.add(misleadingTerm);
        facts.addAll(List.of(f1, f2, rule));

        TermDatabase db = new TermDatabase(facts);
        db.finalizeDatabase();

        return db;
    }

    @Test
    public void backtrackingRuleTest1() {
        TermDatabase db = getTermDatabaseWithRule(false);

        ComplexTerm query = new ComplexTerm("loves", List.of(new Atom("john"), new Atom("mary")));

        Substitution result = db.backtrack(query);

        assertTrue(result.isSuccess());
    }

    @Test
    public void backtrackingRuleTest2() {
        TermDatabase db = getTermDatabaseWithRule(false);

        Variable X = new Variable("X");
        ComplexTerm query = new ComplexTerm("loves", List.of(new Atom("john"), X));

        Substitution result = db.backtrack(query);

        assertTrue(result.isSuccess());
        assertEquals(new Atom("mary"), result.getMap().get(X));
    }

    @Test
    public void backtrackingRuleTest3() {
        TermDatabase db = getTermDatabaseWithRule(true);

        ComplexTerm query = new ComplexTerm("loves", List.of(new Atom("john"), new Atom("mary")));

        Substitution result = db.backtrack(query);

        assertTrue(result.isSuccess());
    }

    @Test
    public void backtrackingRuleTest4() {
        TermDatabase db = getTermDatabaseWithRule(true);

        Variable X = new Variable("X");
        ComplexTerm query = new ComplexTerm("loves", List.of(new Atom("john"), X));

        Substitution result = db.backtrack(query);

        assertTrue(result.isSuccess());
        assertEquals(new Atom("mary"), result.getMap().get(X));
    }

}
