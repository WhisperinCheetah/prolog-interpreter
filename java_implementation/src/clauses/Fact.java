package src.clauses;

import src.Structure;
import src.parser.Parser;
import src.Term;
import src.TermDatabase;
import src.simples.Variable;

import java.util.*;

public class Fact extends Clause {
    public static final String factRegex = "[a-z]+[a-zA-Z]*(\\(.*\\))?";

    public List<Term> args;

    public Fact(String functor, List<Term> args, int arity) {
        super(new FunctorType(functor, arity));
        this.args = args;
    }

    public Fact(Fact other) {
        super(other.getFunctorType());
        this.args = new ArrayList<>();
        for (Term arg : other.args) {
            this.args.add(arg.copy());
        }
    }

    public static boolean isFact(String line) {
        return line.matches(factRegex);
    }

    private static List<Term> parseArgs(String body) {
        String[] parts = body.split(",");
        return Arrays.stream(parts).map(Parser::parseChunk).toList();
    }

    public static Fact fromString(String line) {
        String functor = line.split("\\(")[0];

        try {
            List<Term> args = parseArgs(line.substring(line.indexOf("(") + 1, line.indexOf(")")));
            int arity = args.size();
            return new Fact(functor, args, arity);
        } catch (IndexOutOfBoundsException e) {
            return new Fact(functor, new ArrayList<>(), 0);
        }
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
                if (!seen.contains(var.name())) {
                    seen.add(var.name());
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

    @Override
    public boolean execute(TermDatabase db) {
        List<Clause> candidates = db.getFunctorToStructuresMapItem(this.functorType);
        for (Clause candidate : candidates) {
            if (this.resolvesWith(candidate)) {
                return true;
            }
        }

        return false;
    }

    @Override
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
    public Fact copy() {
        return new Fact(this);
    }

    @Override
    public String toString() {
        return String.format("%s(%s)/%d", getFunctorType().functor, args, getArity());
    }

    public boolean resolvesWith(Clause other) {
        if (this.containsVariables()) {
            return false;
        }

        return other.equalsIgnoreVars(this);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Fact otherFact) {
            if (!this.getFunctorType().equals(otherFact.getFunctorType())) {
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

    @Override
    public boolean equalsIgnoreVars(Fact other) {
        if (!this.getFunctorType().equals(other.getFunctorType())) {
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
        return this.getFunctorType().equals(other.getFunctorType());
    }

}
