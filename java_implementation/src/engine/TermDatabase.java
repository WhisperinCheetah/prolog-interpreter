package engine;

import engine.complex.Dynamic;
import engine.complex.Predicate;
import engine.parser.Parser;
import engine.parser.TermParser;
import engine.directives.Initialization;

import java.text.ParseException;
import java.util.*;

public class TermDatabase {

    Initialization init;
    List<Fact> facts;
    Set<FunctorType> dynamics;

    public TermDatabase(List<Fact> facts) {
        this.facts = facts;
    }

    public TermDatabase() {
        this.init = null;
        this.facts = new ArrayList<>();
        this.dynamics = new HashSet<>();
    }

    public void insertFact(Fact fact, int index) {
        this.facts.add(index, fact);
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

    public List<Term> parseQuery(String queryString) throws ParseException {
        List<String> cleanString = Parser.splitByComma(Parser.cleanString(queryString));

        List<Term> queryTerms = new ArrayList<>();
        for (String cleanTerm : cleanString) {
            Optional<Term> term = TermParser.parse(cleanTerm);

            if (term.isEmpty()) {
                throw new ParseException("Could not parse query", 0);
            }

            queryTerms.add(term.get());
        }

        return queryTerms;
    }

    public Substitution backtrackRecursive(List<Term> queries, int index, Substitution substitution) {
        if (index == queries.size() || substitution.isFailure()) {
            return substitution;
        }

        Term query = queries.get(index).substituteVariables(substitution);

        if (query instanceof Predicate predicate) {
            return backtrackRecursive(queries, index + 1, substitution.unify(predicate.execute()));
        }

        if (query instanceof Dynamic dynamic) {
            return backtrackRecursive(queries, index + 1, substitution.unify(dynamic.execute(this)));
        }

        for (Fact fact : facts) {
            Fact rfact = fact.renameVariables(new HashMap<>());

            Substitution res = rfact.unify(query);

            if (res.isSuccess() && rfact instanceof Rule rule) {
                Substitution ruleRes = backtrackRecursive(rule.getBody(), 0, res);

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

    public Substitution backtrackMultiple(List<Term> queries) {
        return backtrackRecursive(queries, 0, Substitution.success());
    }

    public void runQuery(String queryString) throws ParseException {
        Substitution initializationResult = this.runInitialization();

        if (initializationResult.isFailure()) {
            System.out.println("Initialization goal failed");
            System.out.println(initializationResult);
            return;
        }

        List<Term> query = this.parseQuery(queryString);

        Substitution res = backtrackMultiple(query);

        System.out.println(res);
    }

    public Substitution runInitialization() {
        if (this.init == null) {
            return Substitution.success();
        }

        return this.backtrack(this.init.getGoal());
    }

    public void nextState() {}

    @Override
    public String toString() {
        return "TermDatabase " + facts;
    }
}
