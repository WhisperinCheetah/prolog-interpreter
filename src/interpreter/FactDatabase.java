package interpreter;

import interpreter.complex.ComplexTerm;
import interpreter.complex.dynamic.Dynamic;
import interpreter.complex.predicate.Cut;
import interpreter.complex.predicate.Predicate;
import interpreter.directives.DynamicDirective;
import parser.Parser;
import parser.StringCleaner;
import parser.TermParser;
import interpreter.directives.Initialization;
import interpreter.simple.Variable;

import java.text.ParseException;
import java.util.*;

public class FactDatabase {

    Initialization init;
    List<Fact> facts;
    Set<FunctorType> dynamics;

    public FactDatabase() {
        this.init = null;
        this.facts = new ArrayList<>();
        this.dynamics = new HashSet<>();
    }

    /**
     * This function is used by asserta to insert a fact at the start of the database.
     *
     * @param fact The fact to be inserted in the database
     * @param index The index where it has to be inserted
     */
    public void insertFact(Fact fact, int index) {
        this.facts.add(index, fact);
    }

    /**
     * @param fact Adds a fact to the database
     */
    public void addFact(Fact fact) {
        facts.add(fact);
    }

    /**
     * @param init Adds a initialization to the database
     */
    public void addInitialization(Initialization init) {
        this.init = init;
    }

    /**
     * @param dynamic A DynamicDirective to be added to the database
     */
    public void addDynamic(DynamicDirective dynamic) {
        this.dynamics.add(dynamic.getGoal().getType());
    }


    /**
     * @param fact the fact that is tested to be dynamic
     * @return true if the fact is dynamic
     */
    public boolean isDynamic(Fact fact) {
        if (fact instanceof Rule rule) return dynamics.contains(rule.head.getType());
        if (fact instanceof Predicate) return false;
        if (fact instanceof Dynamic) return false;
        if (fact instanceof ComplexTerm complexTerm) return dynamics.contains(complexTerm.getType());

        return false;
    }

    public int size() {
        return this.facts.size();
    }

    public List<Fact> getFacts() {
        return this.facts;
    }

    public Set<FunctorType> getDynamics() {
        return this.dynamics;
    }

    public List<FunctorType> getDynamicsList() {
        return new ArrayList<>(this.dynamics);
    }


    /**
     * Tries to parse a query.
     *
     * @param queryString The user input
     * @return A list of parsed Terms
     * @throws ParseException If the input could not be parsed
     */
    public List<Term> parseQuery(String queryString) throws ParseException {
        List<String> cleanStrings = Parser.splitByCommaAndSemicolon(queryString).stream().map(StringCleaner::cleanString).toList();

        List<Term> queryTerms = new ArrayList<>();
        for (String cleanTerm : cleanStrings) {
            Optional<Term> term = TermParser.parse(cleanTerm);

            if (term.isEmpty()) {
                throw new ParseException("Could not parse query", 0);
            }

            queryTerms.add(term.get());
        }

        HashMap<String, Variable> varMap = new HashMap<>();
        List<Term> renamedQueryTerms = queryTerms.stream().map(t -> t.renameVariables(varMap)).toList();

        return renamedQueryTerms;
    }

    /**
     * The heart of the database. Tries to backtrack a (multiple) query(s) and returns their unification.
     *
     * @param queries A list of queries that need to get unified
     * @param index Current query index
     * @param unification The current unification
     * @return The unified unification from all the queries
     */
    public Unification backtrackRecursive(List<Term> queries, int index, Unification unification) {
        if (index == queries.size() || unification.isFailure()) {
            return unification;
        }

        Term query = queries.get(index).substituteVariables(unification);

        if (query instanceof Cut) {
            return Unification.cut().unify(backtrackRecursive(queries, index + 1, unification));
        }

        if (query instanceof Predicate predicate) {
            return backtrackRecursive(queries, index + 1, unification.unify(predicate.execute()));
        }

        if (query instanceof Dynamic dynamic) {
            return backtrackRecursive(queries, index + 1, unification.unify(dynamic.execute(this)));
        }

        for (Fact fact : facts) {
            Fact rfact = fact.renameVariables(new HashMap<>());

            Unification res = rfact.unify(query);

            if (res.isSuccess() && rfact instanceof Rule rule) {
                List<Term> mergedQueries = new ArrayList<>(queries);
                mergedQueries.addAll(index + 1, rule.getBody());

                Unification recursiveRes = backtrackRecursive(mergedQueries, index + 1, unification.unify(res));

                res = res.unify(recursiveRes);

                if (recursiveRes.isSuccess() || recursiveRes.isCut()) {
                    return recursiveRes;
                }
            }

            if (res.isSuccess()) {
                Unification recursiveRes = backtrackRecursive(queries, index+1, unification.unify(res));

                if (recursiveRes.isSuccess() || recursiveRes.isCut()) {
                    return recursiveRes;
                }
            }
        }

        return Unification.failure(unification.isCut());
    }

    public Unification backtrack(Term query) {
        return backtrackRecursive(List.of(query), 0, Unification.success());
    }

    public Unification backtrackMultiple(List<Term> queries) {
        return backtrackRecursive(queries, 0, Unification.success());
    }


    /**
     * Takes user input as a query, parses it and tries to unify it in the database. Prints the result.
     *
     * @param queryString The user input
     * @throws ParseException if the input failed to parse
     */
    public void runQuery(String queryString) throws ParseException {
        Unification initializationResult = this.runInitialization();

        // System.out.println(initializationResult);

        if (initializationResult.isFailure()) {
            System.out.println("Initialization goal failed");
            System.out.println(initializationResult);
            return;
        }

        List<Term> query = this.parseQuery(queryString);

        Unification res = backtrackMultiple(query);

        System.out.println(res);

        System.out.println(res.toPrettyString(query));
    }


    /**
     * Runs the initialization goal if it exists.
     *
     * @return The unification from the backtracking
     */
    public Unification runInitialization() {
        if (this.init == null) {
            return Unification.success();
        }

        return this.backtrack(this.init.getGoal());
    }

    public void nextState() {}

    @Override
    public String toString() {
        return "TermDatabase " + facts;
    }
}
