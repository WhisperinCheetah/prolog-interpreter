import java.util.*;

public class TermDatabase {

    List<Term> terms;
    Map<FunctorType, List<List<Term>>> candidateLists;

    Fact lastQuery;
    List<Iterator<Term>> lastQueryCandidateStates;

    public TermDatabase(List<Term> terms) {
        this.terms = terms;
        this.initialiseCandidateLists();
    }

    private List<List<Term>> getOrCreateCandidateLists(Fact fact) {
        return this.candidateLists.computeIfAbsent(fact.functor, f -> {
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

                for (int ruleIndex : rule.head.getVariableIndices(var)) {
                    lists.get(ruleIndex).addAll(candidateLists.getOrDefault(bodyPart.functor, new ArrayList<>()).get(i));
                }
            }
        }
    }

    private void initialiseCandidateLists() {
        this.candidateLists = new HashMap<>();

        for (Term term : terms) {
            if (term instanceof Fact fact) {
                List<List<Term>> lists = getOrCreateCandidateLists(fact);

                List<Term> args = fact.args;
                for (int i = 0; i < args.size(); i++) {
                    if (!(args.get(i) instanceof Variable)) {
                        lists.get(i).add(args.get(i));
                    }
                }
            }
        }

        for (Term term : terms) {
            if (term instanceof Rule rule) {
                List<List<Term>> lists = getOrCreateCandidateLists(rule.head);

                for (Term bodyTerm : rule.body) {
                    if (bodyTerm instanceof Fact bodyPart) {
                        appendRuleListFromBodyPart(rule, bodyPart, lists);
                    }
                }

                System.out.println(this.candidateLists.get(rule.head.functor));
            }
        }
    }

    public boolean resolveQuery(Fact query) {
        for (Term term : this.terms) {
            if (term.resolve(this, query)) {
                return true;
            }
        }

        return false;
    }

    private Fact fillVariablesAndResolve(Fact query, List<Iterator<Term>> iterators, int varc) {
        if (varc == 0) {
            if (resolveQuery(query)) return query;
            return null;
        }

        int argi = query.firstVariableIndex();

        if (argi < 0) return null;

        Iterator<Term> it = iterators.get(argi);

        while (it.hasNext()) {
            Fact copy = new Fact(query);
            copy.fillVariable(argi, it.next());
            Fact bnbResult = fillVariablesAndResolve(copy, iterators, varc-1);

            if (bnbResult != null) {
                return bnbResult;
            }
        }

        return null;
    }

    public Fact unifyQuery(Fact query, List<Iterator<Term>> iterators) {
        int varc = query.countUniqueVariables();

        return fillVariablesAndResolve(query, iterators, varc);
    }

    private void printResults(Fact query, Fact result) {
        Map<Variable, Term> variableMap = query.filledVariables(result);

        if (variableMap == null) {
            System.out.println("FAIL");
        } else {
            variableMap.forEach((variable, term) -> System.out.println(variable + " = " + term));
        }
    }

    private List<Iterator<Term>> initialiseIterators(Fact query) {
        List<Iterator<Term>> iterators = new ArrayList<>();
        for (List<Term> candidateList : this.candidateLists.get(query.functor)) {
            iterators.add(candidateList.iterator());
        }

        return iterators;
    }

    public void runQuery(String queryString) {
        if (!queryString.startsWith("?-")) {
            return;
        }

        String cleanQueryString = queryString.substring(2).replaceAll("\\s+", "");
        Term query = Parser.parseChunk(cleanQueryString);

        assert query instanceof Fact;

        Fact queryFact = (Fact) query;

        if (queryFact.containsVariables()) {
            List<Iterator<Term>> iterators = initialiseIterators(queryFact);
            this.lastQuery = queryFact;
            this.lastQueryCandidateStates = iterators;

            Fact result = this.unifyQuery(queryFact, iterators);
            this.printResults(queryFact, result);
        } else {
            boolean isTrue = this.resolveQuery(queryFact);
            System.out.println(isTrue);
        }
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
                terms +
                "\n" +
                candidateLists;
    }
}
