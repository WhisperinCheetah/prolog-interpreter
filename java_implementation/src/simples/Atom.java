package src.simples;

public class Atom extends Constant<String> {
    public Atom(String value) {
        super(value);
    }

    public static boolean isAtom(String line) {
        return line.matches("[a-z][a-zA-Z]*") || line.matches("'.*'");
    }

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

    @Override
    public Atom copy() {
        return new Atom(value);
    }
}
