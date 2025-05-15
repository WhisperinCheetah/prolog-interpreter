import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class E2eTests {

    /**
     * Does not contain test files that rely on disjunction (;/2) operator.
     */
    private final List<String> paths = List.of(
            "examples/basics/arithmetics.pl",
            "examples/basics/backtracking.pl",
            "examples/basics/cut.pl",
            "examples/basics/equality.pl",
            "examples/basics/fraternity.pl",
            "examples/basics/liberty.pl",
            "examples/basics/write.pl"
    );

    private String runProgramLocal(String path) throws Exception {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        String[] command = { "-s", path, "--testing" };
        Main.main(command);
        String result = outContent.toString();

        System.setOut(originalOut);

        return result;
    }

    private String runProgramSwipl(String path) throws IOException, InterruptedException {
        List<String> command = new ArrayList<>();
        command.add("swipl");
        command.add("-q");
        command.add("-s");
        command.add(path);

        // Optionally add a goal to run and halt
        command.add("-g");
        command.add("halt.");

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectError(ProcessBuilder.Redirect.to(new File("/dev/null"))); // disable warnings the easy way
        Process process = pb.start();

        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
        }
        int exitCode = process.waitFor();

        return result.toString();
    }

    @Test
    public void parseAndRunProgramsTests() throws Exception {
        for (String path : paths) {
            String swiplRes = runProgramSwipl(path);
            String localRes = runProgramLocal(path);

            assertEquals(swiplRes, localRes);
        }
    }
}
