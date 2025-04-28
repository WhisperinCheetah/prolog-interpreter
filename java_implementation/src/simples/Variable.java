package src.simples;

public record Variable(String name) implements SimpleTerm {
    public static boolean isVariable(String line) {
        return line.matches("([A-Z]|_)[a-zA-Z]*");
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Variable(String name1)) {
            return this.name.equals(name1);
        }

        return false;
    }

    @Override
    public Variable copy() {
        return new Variable(this.name);
    }
}
