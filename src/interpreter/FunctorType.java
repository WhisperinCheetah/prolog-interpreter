package interpreter;

public class FunctorType {
    public final String functor;
    public final int arity;

    public FunctorType(String functor, int arity) {
        this.functor = functor;
        this.arity = arity;
    }

    public String getFunctor() {
        return functor;
    }

    public int getArity() {
        return arity;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FunctorType otherType) {
            return functor.equals(otherType.functor) && arity == otherType.arity;
        }

        return false;
    }

    @Override
    public String toString() {
        return functor + "/" + arity;
    }

    @Override
    public int hashCode() {
        return functor.hashCode() + arity;
    }
}
