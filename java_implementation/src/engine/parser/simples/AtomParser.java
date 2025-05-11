package engine.parser.simples;

import engine.simple.Atom;

import java.util.Optional;

public class AtomParser {

    public static Atom parseAtom(String input) {
        if (input.startsWith("'") || input.startsWith("\"")) {
            return new Atom(input.substring(1, input.length() - 1));
        }

        return new Atom(input);
    }

    public static Optional<Atom> parse(String input) {
        if (Atom.isAtom(input)) {
            return Optional.of(parseAtom(input));
        }

        return Optional.empty();
    }
}
