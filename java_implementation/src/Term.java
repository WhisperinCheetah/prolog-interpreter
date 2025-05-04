package src;

public interface Term extends Fact {
    Substitution unify(Term other);
    Term substituteVariables(Substitution substitution);
    Term copy();
}
