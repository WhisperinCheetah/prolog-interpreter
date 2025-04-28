public class Atom implements Term {
    String value;

    public static boolean isAtom(String line) {
        return line.matches("[a-z][a-zA-Z]*");
    }

    public Atom(String value) {
        this.value = value;
    }

    public void fillVariable(Variable _var, Term _fill) {}

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Atom otherAtom) {
            return value.equals(otherAtom.value);
        }

        return false;
    }
}
