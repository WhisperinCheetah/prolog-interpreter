package src.predicates;

import src.clauses.FunctorType;

public abstract class BuiltinPredicate extends Predicate {
    public BuiltinPredicate(FunctorType type) {
        super(type);
    }
}
