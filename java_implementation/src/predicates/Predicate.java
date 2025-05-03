package src.predicates;

import src.Structure;
import src.Term;
import src.clauses.FunctorType;

import java.util.List;

public abstract class Predicate extends Structure {
    public Predicate(FunctorType functor) {
        super(functor);
    }
}