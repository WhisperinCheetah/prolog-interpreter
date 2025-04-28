import java.util.*;

public class Fact implements Term {
    public FunctorType functor;
    public List<Term> args;

    public Fact(String functor, List<Term> args, int arity) {
        this.functor = new FunctorType(functor, arity);
        this.args = args;
    }

    public Fact(Fact other) {
        this.functor = other.functor;
        this.args = new ArrayList<>(other.args);
    }

    public int getArity() {
        return this.functor.arity;
    }

    public String getFunctorName() {
        return this.functor.functor;
    }

    public static boolean isStructure(String line) {
        return line.matches("[a-z]+[a-zA-Z]*\\([^\\-:]*\\)");
    }

    private static List<Term> parseArgs(String body) {
        String[] parts = body.split(",");
        return Arrays.stream(parts).map(Parser::parseChunk).toList();
    }

    public static Fact fromString(String line) {
        String functor = line.split("\\(")[0];
        List<Term> args = parseArgs(line.substring(line.indexOf("(") + 1, line.indexOf(")")));
        int arity = args.size();

        return new Fact(functor, args, arity);
    }

    public boolean resolve(TermDatabase db, Fact query) {
        return this.equalsIgnoreVars(query);
    }

    @Override
    public Fact unify(TermDatabase db, Fact query) {
        return null;
    }

    public boolean containsVariables() {
        for (Term term : args) {
            if (term instanceof Variable) {
                return true;
            }
        }

        return false;
    }

    public int countUniqueVariables() {
        Set<String> seen = new HashSet<>();

        int count = 0;
        for (Term term : args) {
            if (term instanceof Variable var) {
                if (!seen.contains(var.name)) {
                    seen.add(var.name);
                    count++;
                }
            }
        }

        return count;
    }

    public int firstVariableIndex() {
        for (int i = 0; i < args.size(); i++) {
            Term term = args.get(i);
            if (term instanceof Variable) {
                return i;
            }
        }

        return -1;
    }

    public void fillVariable(Variable var, Term fill) {
        for (int i = 0; i < args.size(); i++) {
            Term term = args.get(i);
            if (term.equals(var)) {
                this.args.set(i, fill);
            }
        }
    }

    public void fillVariable(int argi, Term fill) {
        Variable var = (Variable) this.args.get(argi);

        this.args.set(argi, fill);

        for (int i = argi; i < args.size(); i++) {
            Term arg = args.get(i);
            if (arg.equals(var)) {
                this.args.set(i, fill);
            }
        }
    }

    public HashMap<Variable, Term> filledVariables(Fact other) {
        if (other == null) {
            return null;
        }

        HashMap<Variable, Term> map = new HashMap<>();
        for (int i = 0; i < args.size(); i++) {
            Term arg = args.get(i);
            if (arg instanceof Variable var) {
                map.put(var, other.args.get(i));
            }
        }

        return map;
    }

    public List<Integer> getVariableIndices(Variable var) {
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < args.size(); i++) {
            Term arg = args.get(i);
            if (arg.equals(var)) {
                indices.add(i);
            }
        }

        return indices;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)/%d", functor.functor, args, functor.arity);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Fact otherFact) {
            if (!this.functor.equals(otherFact.functor)) {
                return false;
            }

            for (int i = 0; i < this.getArity(); i++) {
                if (!otherFact.args.get(i).equals(this.args.get(i))) {
                    return false;
                }
            }

            return true;

        }

        return false;
    }

    public boolean equalsIgnoreVars(Fact other) {
        if (!this.functor.equals(other.functor)) {
            return false;
        }

        for (int i = 0; i < this.getArity(); i++) {
            if (! (other.args.get(i) instanceof Variable || this.args.get(i) instanceof Variable || this.args.get(i).equals(other.args.get(i))) ) {
                return false;
            }
        }

        return true;
    }

    public boolean shallowEquals(Fact other) {
        return this.functor.equals(other.functor);
    }
}
