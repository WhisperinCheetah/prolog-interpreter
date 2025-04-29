package src.predicates;

import src.Structure;
import src.Term;

import java.util.List;

public abstract class Predicate extends Structure {
    abstract boolean execute();
}