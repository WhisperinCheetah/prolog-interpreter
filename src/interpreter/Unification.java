package interpreter;

import interpreter.complex.ComplexTerm;
import interpreter.simple.Variable;

import java.util.*;

public class Unification {
    private final boolean isCut;
    private final boolean success;
    private final HashMap<Variable, Term> varTermMap;

    public Unification(boolean success, HashMap<Variable, Term> varTermMap) {
        this.success = success;
        this.varTermMap = varTermMap;
        this.isCut = false;
    }

    public Unification(boolean success) {
        this.success = success;
        this.varTermMap = new HashMap<>();
        this.isCut = false;
    }

    public Unification(boolean success, HashMap<Variable, Term> varTermMap, boolean isCut) {
        this.success = success;
        this.varTermMap = varTermMap;
        this.isCut = isCut;
    }

    public static Unification success() {
        return new Unification(true, new HashMap<>());
    }

    public static Unification success(boolean cut) {
        return new Unification(true, new HashMap<>(), cut);
    }

    public static Unification failure() {
        return new Unification(false, new HashMap<>());
    }

    public static Unification failure(boolean cut) {
        return new Unification(false, new HashMap<>(), cut);
    }

    public static Unification fromBoolean(boolean success) {
        return new Unification(success, new HashMap<>());
    }

    public static Unification fromEntry(Variable var, Term term) {
        HashMap<Variable, Term> varTermMap = new HashMap<>();
        varTermMap.put(var, term);
        return new Unification(true, varTermMap);
    }

    public static Unification cut() {
        return new Unification(true, new HashMap<>(), true);
    }

    public Unification unify(Unification other) {
        if (this.isFailure() || other.isFailure()) return Unification.failure(this.isCut || other.isCut);
        HashMap<Variable, Term> map = new HashMap<>(this.varTermMap);
        for (Map.Entry<Variable, Term> entry : other.varTermMap.entrySet()) {
            if (!varTermMap.containsKey(entry.getKey()) || varTermMap.get(entry.getKey()).equals(entry.getValue())) {
                map.put(entry.getKey(), entry.getValue());
            }

            else return Unification.failure(this.isCut || other.isCut);
        }

        return new Unification(true, map, this.isCut || other.isCut);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFailure() {
        return !success;
    }

    public boolean isCut() {
        return isCut;
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
