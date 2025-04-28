public interface Term {
    /**
     * Function that fills all variables var with fill
     *
     * @param var: variable to be filled
     * @param fill: term that is used to fill variable
     */
    void fillVariable(Variable var, Term fill);

    /**
     * Function that takes a database and a query and returns true if the
     * Term and query resolve together.
     *
     * @param db: database
     * @param query: query to resolve
     * @return true if query is resolved with database
     */
    boolean resolve(TermDatabase db, Fact query);

    /**
     * Function that tries to fill in variables of a query
     *
     * @param db: database
     * @param query: the query to be filled in
     * @return a filled in Fact or NULL
     */
    Fact unify(TermDatabase db, Fact query);
}
