package src;

import src.clauses.Clause;
import src.clauses.Fact;
import src.clauses.FunctorType;
import src.clauses.Rule;
import src.directives.Initialization;
import src.simples.Variable;

import java.util.*;

public class TermDatabase {

    Initialization init;

    List<Clause> clauses;
    Map<FunctorType, List<Clause>> functorToStructures;

    Map<FunctorType, List<List<Term>>> candidateLists;

    Fact lastQuery;
    List<Iterator<Term>> lastQueryCandidateStates;

    FunctorType lastFunctor;
    int lastFunctorIndex;

    boolean lastQueryHadVariables;

    public TermDatabase(List<Clause> clauses) {
        this.clauses = clauses;
        this.initialiseCandidateLists();
    }

    public TermDatabase() {
        this.init = null;

        this.clauses = new ArrayList<>();
        this.functorToStructures = new HashMap<>();
        this.candidateLists = new HashMap<>();

        this.lastQuery = null;
        this.lastQueryCandidateStates = new ArrayList<>();

        this.lastFunctor = null;
        this.lastFunctorIndex = -1;
        this.lastQueryHadVariables = false;
    }

    public void addClause(Clause clause) {
        this.clauses.add(clause);
    }

    public void addInitialization(Initialization init) {
        this.init = init;
    }

    public int size() {
        return this.clauses.size();
    }

    public List<Clause> getClauses() {
        return this.clauses;
    }

    public void runInitialization() {
        if (this.init != null) {
            this.init.run(this);
        }
    }

    private List<List<Term>> getOrCreateCandidateLists(Fact fact) {
        return this.candidateLists.computeIfAbsent(fact.getFunctorType(), f -> {
            List<List<Term>> newLists = new ArrayList<>();
            for (int j = 0; j < fact.getArity(); j++) {
                newLists.add(new ArrayList<>());
            }
            return newLists;
        });
    }

    private void appendRuleListFromBodyPart(Rule rule, Fact bodyPart, List<List<Term>> lists) {
        for (int i = 0; i < bodyPart.getArity(); i++) {
            Term arg = bodyPart.args.get(i);
            if (arg instanceof Variable var) {

                for (int ruleIndex : rule.getHead().getVariableIndices(var)) {
                    lists.get(ruleIndex).addAll(candidateLists.getOrDefault(bodyPart.getFunctorType(), new ArrayList<>()).get(i));
                }
            }
        }
    }

    private void initialiseCandidateLists() {
        this.candidateLists = new HashMap<>();

        for (Clause clause : clauses) {
            if (clause instanceof Fact fact) {
                List<List<Term>> lists = getOrCreateCandidateLists(fact);

                List<Term> args = fact.args;
                for (int i = 0; i < args.size(); i++) {
                    if (!(args.get(i) instanceof Variable)) {
                        lists.get(i).add(args.get(i));
                    }
                }
            }
        }

        for (Clause clause : clauses) {
            if (clause instanceof Rule rule) {
                List<List<Term>> lists = getOrCreateCandidateLists(rule.getHead());

                for (Structure bodyTerm : rule.getBody()) {
                    if (bodyTerm instanceof Fact bodyPart) {
                        appendRuleListFromBodyPart(rule, bodyPart, lists);
                    }
                }

                System.out.println(this.candidateLists.get(rule.getFunctorType()));
            }
        }
    }

    private void initialiseFunctorToStructuresMap() {
        for (Clause clause : clauses) {
            this.functorToStructures
                    .computeIfAbsent(clause.getFunctorType(), k -> new ArrayList<>())
                    .add(clause);
        }
    }

    private void setInitializationGoalReference() {
        if (this.init != null) {
            List<Clause> actualGoalCandidates = this.functorToStructures.get(init.getGoal().getFunctorType());
            if (actualGoalCandidates != null && !actualGoalCandidates.isEmpty()) {
                this.init.setGoal(actualGoalCandidates.getFirst());
            }
        }
    }

    public void finalizeDatabase() {
        this.initialiseCandidateLists();
        this.initialiseFunctorToStructuresMap();
        this.setInitializationGoalReference();
    }

    public Map<FunctorType, List<Clause>> getFunctorToStructuresMap() {
        return this.functorToStructures;
    }

    public List<Clause> getFunctorToStructuresMapItem(FunctorType type) {
        return this.functorToStructures.getOrDefault(type, new ArrayList<>());
    }

    private boolean hasUnified(Fact query) {
        List<Clause> candidates = this.getFunctorToStructuresMapItem(query.getFunctorType());
        for (int i = 0; i < candidates.size(); i++) {
            Clause candidate = candidates.get(i);
            if (query.resolvesWith(candidate)) {
                this.lastFunctor = query.getFunctorType();
                this.lastFunctorIndex = i;

                return true;
            }
        }

        return false;
    }

    private Fact fillVariablesAndExecute(Fact query, List<Iterator<Term>> iterators, int varc) {
        if (varc == 0) {
            if (this.hasUnified(query)) return query;
            return null;
        }

        int argi = query.firstVariableIndex();

        if (argi < 0) return null;

        Iterator<Term> it = iterators.get(argi);

        while (it.hasNext()) {
            Fact copy = new Fact(query);
            copy.fillVariable(argi, it.next());
            Fact bnbResult = fillVariablesAndExecute(copy, iterators, varc-1);

            if (bnbResult != null) {
                return bnbResult;
            }
        }

        return null;
    }

    public Fact unifyQuery(Fact query, List<Iterator<Term>> iterators) {
        int varc = query.countUniqueVariables();

        return fillVariablesAndExecute(query, iterators, varc);
    }

    private void executePostUnification() {
        List<Clause> possibleClauses = this.functorToStructures.get(this.lastFunctor);
        if (possibleClauses != null && !possibleClauses.isEmpty()) {
            possibleClauses.get(this.lastFunctorIndex).execute(this);
        }
    }

    private void printResults(Fact query, Fact result) {
        if (!lastQueryHadVariables) {
            this.executePostUnification();
            System.out.println();
            if (result != null) {
                System.out.println("true");
            } else {
                System.out.println("false");
            }
        } else {
            System.out.println();
            if (result == null) {
                System.out.println("FAIL");
            } else {
                Map<Variable, Term> variableMap = query.filledVariables(result);
                variableMap.forEach((variable, term) -> System.out.println(variable + " = " + term));
            }
        }
    }

    private List<Iterator<Term>> initialiseIterators(Fact query) {
        List<Iterator<Term>> iterators = new ArrayList<>();
        for (List<Term> candidateList : this.candidateLists.getOrDefault(query.getFunctorType(), new ArrayList<>())) {
            iterators.add(candidateList.iterator());
        }

        return iterators;
    }

    public void setState(Fact query) {
        List<Iterator<Term>> iterators = initialiseIterators(query);
        this.lastQuery = query;
        this.lastQueryCandidateStates = iterators;

        this.lastQueryHadVariables = query.containsVariables();
    }

    public void runQuery(String queryString) {
        if (!queryString.startsWith("?-")) {
            return;
        }

        this.runInitialization();

        String cleanQueryString = queryString.substring(2).replaceAll("\\s+", "");
        Fact query = Fact.fromString(cleanQueryString);

        this.setState(query);

        Fact result = this.unifyQuery(query, this.lastQueryCandidateStates);
        this.printResults(query, result);
    }

    public void nextState() {
        if (this.lastQuery == null) {
            return;
        }

        Fact res = this.unifyQuery(this.lastQuery, this.lastQueryCandidateStates);
        this.printResults(this.lastQuery, res);
    }

    @Override
    public String toString() {

        return "TermDatabase\n" +
                clauses +
                "\n" +
                candidateLists;
    }
}
