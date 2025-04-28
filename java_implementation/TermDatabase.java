import java.util.*;

public class TermDatabase {

    List<Term> terms;
    Map<FunctorType, List<List<Term>>> candidateLists;

    public TermDatabase(List<Term> terms) {
        this.terms = terms;
        this.initialiseCandidateLists();
    }

    private void initialiseCandidateLists() {
        this.candidateLists = new HashMap<>();

        for (Term term : terms) {
            if (term instanceof Fact fact) {

                if (!this.candidateLists.containsKey(fact.functor)) {
                    List<List<Term>> lists = new ArrayList<>();
                    for (int j = 0; j < fact.getArity(); j++) {
                        lists.add(new ArrayList<>());
                    }
                    this.candidateLists.put(fact.functor, lists);
                }

                List<Term> args = fact.args;
                for (int i = 0; i < args.size(); i++) {
                    if (!(args.get(i) instanceof Variable)) {
                        this.candidateLists.get(fact.functor).get(i).add(args.get(i));
                    }
                }
            }
        }
    }

    public boolean resolve(Fact query) {
        for (Term term : this.terms) {

            term.resolve(this, query);

            if (term instanceof Fact fact && fact.equalsIgnoreVars(query))  {
                return true;
            }

            if (term instanceof Rule rule && query.shallowEquals(rule.head)) {
                Rule copy = new Rule(rule);
                copy.fill(query);

            }
        }

        return false;
    }

    private Fact fillVariablesAndResolve(Fact query, int varc) {
        if (varc == 0) {
            if (resolve(query)) return query;
            return null;
        }

        int argi = query.firstVariableIndex();

        if (argi < 0) return null;

        List<Term> candidates = this.candidateLists.get(query.functor).get(argi);

        for (Term candidate : candidates) {
            Fact copy = new Fact(query);
            copy.fillVariable(argi, candidate);
            Fact bnbResult = fillVariablesAndResolve(copy, varc-1);

            if (bnbResult != null) {
                return bnbResult;
            }
        }

        return null;
    }

    public Fact unify(Fact query) {
        int varc = query.countUniqueVariables();

        return fillVariablesAndResolve(query, varc);
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
            Fact result = this.unify(queryFact);
            Map<Variable, Term> variableMap = queryFact.filledVariables(result);

            variableMap.forEach((variable, term) -> {
                System.out.println(variable + " = " + term);
            });
        } else {
            boolean isTrue = this.resolve(queryFact);
            System.out.println(isTrue);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TermDatabase\n");
        sb.append(terms);
        sb.append("\n");
        sb.append(candidateLists);

        return sb.toString();
    }
}
