package engine.parser;

import engine.Rule;

import java.util.List;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StringCleaner {

    static boolean isOperator(char c) {
        return "+-*/^".indexOf(c) != -1;
    }

    static int precedence(char op) {
        return switch (op) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^'       -> 3;
            default        -> -1;
        };
    }

    static boolean isRightAssociative(char op) {
        return op == '^';
    }

    static String reverseAndSwapParentheses(String expr) {
        StringBuilder result = new StringBuilder();
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == '(') result.append(')');
            else if (c == ')') result.append('(');
            else result.append(c);
        }
        return result.toString();
    }

    public static String infixToFunctionalPrefix(String expr) {
        String noWhitespace = StringCleaner.removeAllWhitespaces(expr);

        String reversed = reverseAndSwapParentheses(noWhitespace);

        Stack<String> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < reversed.length(); ) {
            char c = reversed.charAt(i);

            // Variable or number (multi-char)
            if (Character.isLetterOrDigit(c)) {
                StringBuilder token = new StringBuilder();
                while (i < reversed.length() && Character.isLetterOrDigit(reversed.charAt(i))) {
                    token.append(reversed.charAt(i));
                    i++;
                }
                operands.push(token.reverse().toString());
                continue;
            }

            if (c == '(') {
                operators.push(c);
                i++;
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    combineTopOperator(operators, operands);
                }
                if (!operators.isEmpty()) operators.pop(); // remove '('
                i++;
            } else if (isOperator(c)) {
                while (!operators.isEmpty() &&
                        (precedence(operators.peek()) > precedence(c) ||
                                (precedence(operators.peek()) == precedence(c) && !isRightAssociative(c))) &&
                        operators.peek() != '(') {
                    combineTopOperator(operators, operands);
                }
                operators.push(c);
                i++;
            } else {
                throw new IllegalArgumentException("Invalid character: " + c + " while parsing " + expr);
            }
        }

        while (!operators.isEmpty()) {
            combineTopOperator(operators, operands);
        }

        return operands.pop();
    }

    private static void combineTopOperator(Stack<Character> operators, Stack<String> operands) {
        char op = operators.pop();
        String left = operands.pop();
        String right = operands.pop();
        operands.push(op + "(" + left + "," + right + ")");
    }


    public static String _convertToPrefix(String input) {
        String[] operators = {"==", "\\==", "=", "\\=", " is "};
        int parenDepth = 0;
        boolean inQuotes = false;
        char quoteChar = '\0';

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            // Handle quotes
            if ((c == '\'' || c == '"') && (i == 0 || input.charAt(i - 1) != '\\')) {
                if (inQuotes && c == quoteChar) {
                    inQuotes = false;
                } else if (!inQuotes) {
                    inQuotes = true;
                    quoteChar = c;
                }
            }

            if (inQuotes) continue;

            // Track parentheses
            if (c == '(') parenDepth++;
            else if (c == ')') parenDepth--;

            // Only search for operators at top level (outside any nested structures)
            if (parenDepth == 0) {
                for (String op : operators) {
                    if (input.startsWith(op, i)) {
                        String lhs = input.substring(0, i).trim();
                        String rhs = input.substring(i + op.length()).trim();
                        if (op.equals(" is ")) {
                            rhs = StringCleaner.infixToFunctionalPrefix(rhs);
                        }
                        return op.trim() + "(" + lhs + "," + rhs + ")";
                    }
                }
            }
        }

        return input; // No top-level operator found; return as-is
    }

    public static String convertToPrefix(String input) {
        if (!Rule.isRule(input)) {
            return _convertToPrefix(input);
        }

        if (input.contains("\n")) {
            throw new AssertionError("Input string contains line breaks");
        }

        String[] headAndBody = input.split(":-");
        String head = headAndBody[0];

        String body = headAndBody[1];
        List<String> bodyParts = Parser.splitByComma(body);
        String cleanedBody = bodyParts.stream().map(StringCleaner::_convertToPrefix).collect(Collectors.joining(","));

        return head + ":-" + cleanedBody;
    }

    private static String removeAllWhitespaces(String input) {
        return input.replaceAll("\\s+", "");
    }

    public static String removeNonSingleSpaces(String input) {
        StringBuilder output = new StringBuilder();

        boolean inQuotes = false;
        char quoteChar = '\0';

        for (int i = 0; i < input.length() - 1; i++) {
            char c = input.charAt(i);

            // Handle quotes
            if ((c == '\'' || c == '"') && (i == 0 || input.charAt(i - 1) != '\\')) {
                if (inQuotes && c == quoteChar) {
                    inQuotes = false;
                } else if (!inQuotes) {
                    inQuotes = true;
                    quoteChar = c;
                }
            }

            if (inQuotes) {
                output.append(c);
                continue;
            } else {
                if (c == ' ' && input.charAt(i + 1) == ' ') {
                    continue;
                } else {
                    output.append(c);
                }
            }
        }

        output.append(input.charAt(input.length() - 1));

        return output.toString(); // TODO
    }

    public static String removeSingleSpaces(String input) {
        StringBuilder output = new StringBuilder();

        boolean inQuotes = false;
        char quoteChar = '\0';

        for (int i = 0; i < input.length() - 1; i++) {
            char c = input.charAt(i);

            // Handle quotes
            if ((c == '\'' || c == '"') && (i == 0 || input.charAt(i - 1) != '\\')) {
                if (inQuotes && c == quoteChar) {
                    inQuotes = false;
                } else if (!inQuotes) {
                    inQuotes = true;
                    quoteChar = c;
                }
            }

            if (inQuotes) {
                output.append(c);
                continue;
            } else {
                if (c == ' ') {
                    continue;
                } else {
                    output.append(c);
                }
            }
        }

        output.append(input.charAt(input.length() - 1));

        return output.toString();
    }

    public static String cleanString(String input) {
        if (input.trim().isEmpty()) return input;

        List<Function<String, String>> functionStack = List.of(
                String::trim,
                s -> s.charAt(s.length() - 1) == '.' ? s.substring(0, s.length() - 1) : s, // remove '.'
                s -> s.replaceAll("[\\s&&[^ ]]+", ""), // remove non-space whitespace
                StringCleaner::removeNonSingleSpaces, // remove duplicate spaces
                s -> s.replaceAll("^:- ?dynamic ?([a-z]+/\\d+)", ":-dynamic($1)"), // add brackets to dynamic directive
                StringCleaner::convertToPrefix, // convert operators to prefix
                StringCleaner::removeSingleSpaces
        );

        return functionStack.stream()
                .reduce(Function.identity(), Function::andThen)
                .apply(input);
    }
}
