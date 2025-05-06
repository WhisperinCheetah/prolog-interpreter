package engine;

import engine.complex.ComplexTerm;

import engine.parser.Parser;
import engine.parser.TermParser;
import engine.directives.Initialization;

import java.text.ParseException;
import java.util.*;

public class TermDatabase {

    Initialization init;

    List<Fact> facts;
    Iterator<Fact> queryState;
    Map<FunctorType, List<Rule>> functorToStructures;

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


    private Term parseQuery(String queryString) throws ParseException {
        String cleanString = Parser.cleanString(queryString);
        Optional<Term> maybeQuery = TermParser.parse(cleanString);

        if (maybeQuery.isEmpty()) throw new ParseException("Failed to parse " + cleanString, 0);

        return maybeQuery.get();
    }

    public Substitution backtrack(Term query) {
        Substitution res = Substitution.failure();
        for (Fact fact : facts) {
            Fact renamedFact = fact.renameVariables(new HashMap<>());

            res = renamedFact.unify(query);

            if (res.isSuccess() && renamedFact instanceof Rule struct) {
                for (Term term : struct.body) {
                    Term filledTerm = term.substituteVariables(res);
                    res = res.unify(backtrack(filledTerm));
                }
            }

            if (res.isSuccess()) break;
        }

        return res;
    }

    // TODO query's kunnen komma's hebben
    public void runQuery(String queryString) throws ParseException {
        Term query = this.parseQuery(queryString);

        Substitution res = backtrack(query);

        System.out.println(res);
    }

    public void nextState() {}

    @Override
    public String toString() {
        return "TermDatabase " + facts;
    }
}
