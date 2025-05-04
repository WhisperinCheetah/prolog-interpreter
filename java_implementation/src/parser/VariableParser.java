package src.parser;

import src.simple.Variable;

import java.util.Optional;

public class VariableParser {

    public static Variable parseVariable(String variable) {
        return new Variable(variable);
    }

    public static Optional<Variable> parse(String input) {
        if (Variable.isVariable(input)) {
            return Optional.of(parseVariable(input));
        }

        return Optional.empty();
    }
}
