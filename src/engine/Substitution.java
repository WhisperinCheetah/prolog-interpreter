package engine;

import engine.complex.ComplexTerm;
import engine.simple.Variable;

import java.util.*;

public class Substitution {
    private final boolean success;
    private final HashMap<Variable, Term> varTermMap;

    public Substitution(boolean success, HashMap<Variable, Term> varTermMap) {
        this.success = success;
        this.varTermMap = varTermMap;
    }

    public Substitution(boolean success) {
        this.success = success;
        this.varTermMap = new HashMap<>();
    }

    public static Substitution success() {
        return new Substitution(true, new HashMap<>());
    }

    public static Substitution failure() {
        return new Substitution(false, new HashMap<>());
    }

    public static Substitution fromBoolean(boolean success) {
        return new Substitution(success, new HashMap<>());
    }

    public static Substitution fromEntry(Variable var, Term term) {
        HashMap<Variable, Term> varTermMap = new HashMap<>();
        varTermMap.put(var, term);
        return new Substitution(true, varTermMap);
    }

    public Substitution unify(Substitution other) {
        if (this.isFailure() || other.isFailure()) return Substitution.failure();
        HashMap<Variable, Term> map = new HashMap<>(this.varTermMap);
        for (Map.Entry<Variable, Term> entry : other.varTermMap.entrySet()) {
            if (!varTermMap.containsKey(entry.getKey()) || varTermMap.get(entry.getKey()).equals(entry.getValue())) {
                map.put(entry.getKey(), entry.getValue());
            }

            else return Substitution.failure();
        }

        return new Substitution(true, map);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFailure() {
        return !success;
    }

    public HashMap<Variable, Term> getMap() {
        return varTermMap;
    }

    public void insert(Variable var, Term term) {
        varTermMap.put(var, term);
    }

    public String toPrettyString(List<Term> queries) {
        if (varTermMap.isEmpty()) {
            return success ? "true" : "false";
        }

        Set<Variable> queryVariables = new HashSet<>();
        for (Term query : queries) {
            if (query instanceof ComplexTerm complexQuery) {
                queryVariables.addAll(complexQuery.getVariables());
            }
        }

        StringBuilder str = new StringBuilder();
        for (Variable var: queryVariables) {
            Term varRes = this.varTermMap.get(var);
            str.append(var.getName()).append("=").append(varRes.toString());
        }

        return str.toString();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("engine.Substitution[").append(success).append("]\n");
        for (Map.Entry<Variable, Term> entry : varTermMap.entrySet()) {
            s.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        if (varTermMap.isEmpty()) {
            s.append("{ no entries }");
        }

        return s.toString();
    }
}
