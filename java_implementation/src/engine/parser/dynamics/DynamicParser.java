package engine.parser.dynamics;

import engine.complex.dynamic.Dynamic;

import java.util.Optional;

public class DynamicParser {

    public static Optional<Dynamic> parse(String input) {
        Optional<Dynamic> dynamic = AssertaParser.parse(input).map(a -> a);

        return dynamic;
    }
}
