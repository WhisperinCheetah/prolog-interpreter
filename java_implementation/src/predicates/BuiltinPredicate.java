package src.predicates;

import src.TermType;

public abstract class BuiltinPredicate extends Predicate {

    @Override
    public TermType getTermType() {
        return TermType.PREDICATE;
    }
}
