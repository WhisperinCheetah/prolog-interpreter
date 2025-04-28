public class Variable implements Term {
    String name;

    public static boolean isVariable(String line) {
        return line.matches("([A-Z]|_)[a-zA-Z]*");
    }

    public Variable(String name) {
        this.name = name;
    }

    public void fillVariable(Variable _var, Term _fill) {
        return;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Variable otherVariable) {
            return this.name.equals(otherVariable.name);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
