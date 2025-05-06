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


    public Term parseQuery(String queryString) throws ParseException {
        String cleanString = Parser.cleanString(queryString);
        Optional<Term> maybeQuery = TermParser.parse(cleanString);

        if (maybeQuery.isEmpty()) throw new ParseException("Failed to parse " + cleanString, 0);

        return maybeQuery.get();
    }

    public Substitution backtrackRecursive(List<Term> queries, int index, Substitution substitution) {
        if (index == queries.size() || substitution.isFailure()) {
            return substitution;
        }

        Term query = queries.get(index).substituteVariables(substitution);

        for (Fact fact : facts) {
            Fact rfact = fact.renameVariables(new HashMap<>());

            Substitution res = rfact.unify(query);

            if (res.isSuccess() && rfact instanceof Rule rule) {
                Substitution ruleRes = backtrackRecursive(rule.getBody(), index + 1, res);

                res = res.unify(ruleRes);
            }

            if (res.isSuccess()) {
                Substitution recursiveRes = backtrackRecursive(queries, index+1, substitution.unify(res));

                if (recursiveRes.isSuccess()) {
                    return recursiveRes;
                }
            }
        }

        return Substitution.failure();
    }

    public Substitution backtrack(Term query) {
        return backtrackRecursive(List.of(query), 0, Substitution.success());
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
