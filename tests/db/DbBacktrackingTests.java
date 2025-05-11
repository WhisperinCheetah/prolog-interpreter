package db;

import interpreter.Fact;
import interpreter.Rule;
import interpreter.Unification;
import interpreter.FactDatabase;
import interpreter.complex.ComplexTerm;
import interpreter.simple.Atom;
import interpreter.simple.Variable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class DbBacktrackingTests {

    public FactDatabase getTermDatabase() {
        ComplexTerm dbComplex = new ComplexTerm("likes", List.of(new Atom("john"), new Atom("mary")));

        FactDatabase db = new FactDatabase();
        db.addFact(dbComplex);

        return db;
    }

    @Test
    public void backtrackingComplexTermTest1() {
        FactDatabase db = getTermDatabase();

        ComplexTerm query = new ComplexTerm("likes", List.of(new Atom("john"), new Atom("mary")));
        Unification result = db.backtrack(query);

        assertTrue(result.isSuccess());
    }

    @Test
    public void backtrackingComplexTermTest2() {
        FactDatabase db = getTermDatabase();

        ComplexTerm query1 = new ComplexTerm("likes", List.of(new Atom("john"), new Atom("john")));
        Atom query2 = new Atom("john");

        Unification result1 = db.backtrack(query1);
        Unification result2 = db.backtrack(query2);

        assertTrue(result1.isFailure());
        assertTrue(result2.isFailure());
    }

    @Test
    public void backtrackingComplexTermTest3() {
        FactDatabase db = getTermDatabase();

        Variable X = new Variable("X");
        ComplexTerm query = new ComplexTerm("likes", List.of(new Atom("john"), X));

        Unification result = db.backtrack(query);

        assertTrue(result.isSuccess());
        assertEquals(new Atom("mary"), result.getMap().get(X));
    }

    public FactDatabase getTermDatabaseWithVars() {
        ComplexTerm dbComplex = new ComplexTerm("likes", List.of(new Atom("john"), new Variable("X")));

        FactDatabase db = new FactDatabase();
        db.addFact(dbComplex);

        return db;
    }

    @Test
    public void backtrackingComplexTermTest4() {
        FactDatabase db = getTermDatabaseWithVars();

        ComplexTerm query = new ComplexTerm("likes", List.of(new Atom("john"), new Atom("mary")));

        Unification result = db.backtrack(query);

        assertTrue(result.isSuccess());
    }

    @Test
    public void backtrackingComplexTermTest5() {
        FactDatabase db = getTermDatabaseWithVars();

        Variable X = new Variable("X");
        ComplexTerm query = new ComplexTerm("likes", List.of(X, new Atom("mary")));

        Unification result = db.backtrack(query);

        assertTrue(result.isSuccess());
        assertEquals(new Atom("john"), result.getMap().get(X));
    }

    public FactDatabase getTermDatabaseWithRule(boolean misleading) {
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

        FactDatabase db = new FactDatabase();
        facts.forEach(db::addFact);

        return db;
    }

    @Test
    public void backtrackingRuleTest1() {
        FactDatabase db = getTermDatabaseWithRule(false);

        ComplexTerm query = new ComplexTerm("loves", List.of(new Atom("john"), new Atom("mary")));

        Unification result = db.backtrack(query);

        assertTrue(result.isSuccess());
    }

    @Test
    public void backtrackingRuleTest2() {
        FactDatabase db = getTermDatabaseWithRule(false);

        Variable X = new Variable("X");
        ComplexTerm query = new ComplexTerm("loves", List.of(new Atom("john"), X));

        Unification result = db.backtrack(query);

        assertTrue(result.isSuccess());
        assertEquals(new Atom("mary"), result.getMap().get(X));
    }

    @Test
    public void backtrackingRuleTest3() {
        FactDatabase db = getTermDatabaseWithRule(true);

        ComplexTerm query = new ComplexTerm("loves", List.of(new Atom("john"), new Atom("mary")));

        Unification result = db.backtrack(query);

        assertTrue(result.isSuccess());
    }

    @Test
    public void backtrackingRuleTest4() {
        FactDatabase db = getTermDatabaseWithRule(true);

        Variable X = new Variable("X");
        ComplexTerm query = new ComplexTerm("loves", List.of(new Atom("john"), X));

        Unification result = db.backtrack(query);

        assertTrue(result.isSuccess());
        assertEquals(new Atom("mary"), result.getMap().get(X));
    }

}
