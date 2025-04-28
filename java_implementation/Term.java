public interface Term {
    void fillVariable(Variable var, Term fill);
    boolean resolve(TermDatabase db, Fact query);
}
