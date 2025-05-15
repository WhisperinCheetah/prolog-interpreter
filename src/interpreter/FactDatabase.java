package interpreter;

import interpreter.complex.ComplexTerm;
import interpreter.complex.dynamic.Dynamic;
import interpreter.complex.predicate.builtins.Cut;
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
     * Adds a fact to the database
     *
     * @param fact The fact to be added
     */
    public void addFact(Fact fact) {
        facts.add(fact);
    }

    /**
     * Adds an initialization/1 directive to the database
     *
     * @param init The Initialization
     */
    public void addInitialization(Initialization init) {
        this.init = init;
    }

    /**
     * Adds a dynamic/1 directive to the database
     *
     * @param dynamic A DynamicDirective to be added to the database
     */
    public void addDynamic(DynamicDirective dynamic) {
        this.dynamics.add(dynamic.getGoal().getType());
    }

    /**
     * Check if a given fact has been made dynamic in a script (:- dynamic functor-name/arity)
     *
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
     * Try to parse a query.
     *
     * @param queryString The user input
     * @return A list of parsed Terms
     *
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
     * This function is the beating heart of the interpreter. It recursively backtracks a list of queries that
     * need to be unified with the database.
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

        // substitute the variables that are in the unification object
        Term query = queries.get(index).substituteVariables(unification);

        if (query instanceof Cut) {
            return Unification.cut().merge(backtrackRecursive(queries, index + 1, unification));
        }

        if (query instanceof Predicate predicate) {
            return backtrackRecursive(queries, index + 1, unification.merge(predicate.execute()));
        }

        if (query instanceof Dynamic dynamic) {
            return backtrackRecursive(queries, index + 1, unification.merge(dynamic.execute(this)));
        }

        // iterate over all facts in the database
        for (Fact fact : facts) {

            // rename the variables in the fact such that they are 'linked' to each other
            Fact rfact = fact.renameVariables(new HashMap<>());

            // try to unify the query with the fact
            Unification res = rfact.unify(query);

            // if the unification is success and the fact is a rule, add the body of the rule to the list
            // of queries, and try to backtrack them too.
            if (res.isSuccess() && rfact instanceof Rule rule) {
                List<Term> mergedQueries = new ArrayList<>(queries);
                mergedQueries.addAll(index + 1, rule.getBody());

                Unification recursiveRes = backtrackRecursive(mergedQueries, index + 1, unification.merge(res));

                res = res.merge(recursiveRes);

                if (recursiveRes.isSuccess() || recursiveRes.isCut()) {
                    return recursiveRes;
                }
            }

            // otherwise if it's just a success, go on to the next item in the query list
            if (res.isSuccess()) {
                Unification recursiveRes = backtrackRecursive(queries, index+1, unification.merge(res));

                if (recursiveRes.isSuccess() || recursiveRes.isCut()) {
                    return recursiveRes;
                }
            }
        }

        // if no fact returned a success, return a failure
        return Unification.failure(unification.isCut());
    }

    /**
     * Helper-function for starting a recursive backtrack from a single query
     *
     * @param query the query to be backtracked
     * @return a Unification
     */
    public Unification backtrack(Term query) {
        return backtrackRecursive(List.of(query), 0, Unification.success());
    }

    /**
     * Helper-function for starting a recursive backtrack from multiple queries
     *
     * @param queries a list of queries
     * @return a Unification
     */
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
        // run initialization
        Unification initializationResult = this.runInitialization();

        if (initializationResult.isFailure()) {
            System.out.println("Warning: Initialization goal failed");
            return;
        }

        List<Term> query = this.parseQuery(queryString);

        Unification res = backtrackMultiple(query);

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

    @Override
    public String toString() {
        return "TermDatabase " + facts;
    }
}
