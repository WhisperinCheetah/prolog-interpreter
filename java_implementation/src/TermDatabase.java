package src;

import src.complex.ComplexTerm;
import src.directives.Initialization;
import src.parser.ComplexTermParser;
import src.parser.Parser;
import src.simple.Variable;

import java.util.*;

public class TermDatabase {

    Initialization init;

    List<Fact> facts;
    Map<FunctorType, List<Structure>> functorToStructures;

    Map<FunctorType, List<List<Term>>> candidateLists;

    ComplexTerm lastQuery;
    List<Iterator<Term>> lastQueryCandidateStates;

    public TermDatabase(List<Fact> facts) {
        this.facts = facts;
    }

    public TermDatabase() {
        this.init = null;

        this.facts = new ArrayList<>();
        this.functorToStructures = new HashMap<>();
        this.candidateLists = new HashMap<>();

        this.lastQuery = null;
        this.lastQueryCandidateStates = new ArrayList<>();
    }

    public void addFact(Fact fact) {
        facts.add(fact);
    }

    public void addInitialization(Initialization init) {
        this.init = init;
    }

    public int size() {
        return this.facts.size();
    }

    public List<Fact> getFacts() {
        return this.facts;
    }

    public void finalizeDatabase() {}

    private Substitution parseAndRunQuery(String queryString) {
        Optional<ComplexTerm> maybeQuery = ComplexTermParser.parse(Parser.cleanString(queryString));

        if (maybeQuery.isEmpty()) {
            return Substitution.failure();
        }

        ComplexTerm query = maybeQuery.get();

        for (Fact fact : functorToStructures.get(query.getType())) {
            // TODO
        }

        return Substitution.success();
    }

    public void runQuery(String queryString) {
        Substitution result = parseAndRunQuery(queryString);
    }

    public void nextState() {}

    @Override
    public String toString() {

        return "TermDatabase " + facts;
    }
}
