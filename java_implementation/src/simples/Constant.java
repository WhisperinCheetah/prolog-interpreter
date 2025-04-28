package src.simples;

public abstract class Constant<T> implements SimpleTerm {
    protected final T value;

    public Constant(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
