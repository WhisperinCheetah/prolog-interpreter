package src;

import src.simple.Variable;

import java.util.HashMap;
import java.util.Map;

public class Substitution {
    private final boolean success;
    private final HashMap<Variable, Term> varTermMap;

    public Substitution(boolean success, HashMap<Variable, Term> varTermMap) {
        this.success = success;
        this.varTermMap = varTermMap;
    }

    public static Substitution success() {
        return new Substitution(true, new HashMap<>());
    }

    public static Substitution failure() {
        return new Substitution(false, new HashMap<>());
    }

    public static Substitution ofEntry(Variable var, Term term) {
        HashMap<Variable, Term> varTermMap = new HashMap<>();
        varTermMap.put(var, term);
        return new Substitution(true, varTermMap);
    }

    public Substitution unify(Substitution other) {
        if (this.isFailure() || other.isFailure()) return Substitution.failure();
        Substitution newSub = new Substitution(true, new HashMap<>(this.varTermMap));
        for (Map.Entry<Variable, Term> entry : other.varTermMap.entrySet()) {
            if (!varTermMap.containsKey(entry.getKey()) || varTermMap.get(entry.getKey()).equals(entry.getValue())) {
                newSub.insert(entry.getKey(), entry.getValue());
            }

            else return Substitution.failure();
        }

        return newSub;
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
}
