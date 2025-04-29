package src.predicates;

import src.Structure;
import src.Term;
import src.parser.Parser;

import java.util.List;

public class Write extends BuiltinPredicate {

    Term arg;

    public Write(Term arg) {
        this.arg = arg;
    }

    public static boolean isWrite(String line) {
        return line.matches("write\\(.*\\)");
    }

    public static Write fromString(String line) {
        String argString = line.substring("write(".length(), line.length() - 1);
        Term arg = Parser.parseChunk(argString);

        return new Write(arg);
    }

    @Override
    boolean execute() {
        System.out.println(arg.toString());

        return true;
    }

    @Override
    public Write copy() {
        return new Write(arg.copy());
    }
}
