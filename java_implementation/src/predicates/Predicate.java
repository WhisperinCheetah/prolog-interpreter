package src.predicates;

import src.Fact;
import src.Term;

import java.util.List;

public abstract class Predicate implements Fact {
    abstract boolean execute(List<Term> args);
}