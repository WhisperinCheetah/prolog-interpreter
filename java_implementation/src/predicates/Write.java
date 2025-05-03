package src.predicates;

import src.Structure;
import src.Term;
import src.TermDatabase;
import src.clauses.FunctorType;
import src.parser.Parser;
import src.simples.Variable;

import java.util.List;

public class Write extends BuiltinPredicate {

    Term arg;

    public Write(Term arg) {
        super(new FunctorType("write", 1));
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
    public void fillVariable(Variable var, Term fill) {
        if (this.arg instanceof Structure s) {
            s.fillVariable(var, fill);
        }
    }

    @Override
    public boolean execute(TermDatabase db) {
        System.out.print(arg.toString());

        return true;
    }

    @Override
    public Write copy() {
        return new Write(arg.copy());
    }

    @Override
    public String toString() {
        return "write(" + arg.toString() + ")";
    }
}
