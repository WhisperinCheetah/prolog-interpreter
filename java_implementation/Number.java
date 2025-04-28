public class Number implements Term {
    double value;

    public static Number fromString(String line) {
        return new Number(Double.parseDouble(line));
    }

    public Number(double value) {
        this.value = value;
    }

    public void fillVariable(Variable _var, Term _fill) {
        return;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Number otherNumber) {
            return value == otherNumber.value;
        }

        return false;
    }
}
