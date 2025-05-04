package src.parser;

import src.simple.Atom;

import java.util.Optional;

public class AtomParser {

    public static Atom parseAtom(String input) {
        return new Atom(input);
    }

    public static Optional<Atom> parse(String input) {
        if (Atom.isAtom(input)) {
            return Optional.of(parseAtom(input));
        }

        return Optional.empty();
    }
}
