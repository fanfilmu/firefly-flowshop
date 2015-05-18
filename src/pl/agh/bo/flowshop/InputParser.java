package pl.agh.bo.flowshop;

import pl.agh.bo.flowshop.problem.FlowshopProblem;
import pl.agh.bo.flowshop.problem.FlowshopProblemBuilder;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class InputParser {

    private final String filename;
    long initialSeed;
    private List<FlowshopProblem> parsedProblems;

    public InputParser(String filename) throws FileNotFoundException {
        // Get the file handler
        File file = new File(filename);
        if (!file.exists()) throw new FileNotFoundException("Podaj ścieżkę do poprawnego pliku ;)");

        this.filename = filename;
        this.parsedProblems = new LinkedList<>();
    }

    /**
     * Parse the file. Get collections of jobs and initial seed
     */
    public void parse() throws IOException {
        String line;
        String params[];
        String[][] operationTimesStrings;
        int operationTimes[];
        int jobs[][];
        FlowshopProblem problem;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // new problem definition - first line has no meaning
            while (reader.readLine() != null) {
                // global problem parameters expected, e.g.
                // 100 5 896678084 5495 5437
                line = reader.readLine();
                params = line.trim().split("[ ]+");

                problem = new FlowshopProblemBuilder()
                        .setJobCount(params[0])
                        .setOperationCount(params[1])
                        .setInitialSeed(params[2])
                        .setUpperBound(params[3])
                        .setLowerBound(params[4]).build();

                operationTimesStrings = new String[problem.operationCount][problem.jobCount];
                jobs = new int[problem.jobCount][problem.operationCount];

                // skip "processing times" caption
                reader.readLine();

                for (int i = 0; i < problem.operationCount; i++) {
                    // read operation costs
                    operationTimesStrings[i] = reader.readLine().trim().split("[ ]+");
                }

                // build collection of jobs
                for (int i = 0; i < problem.jobCount; i++) {
                    operationTimes = new int[problem.operationCount];
                    for (int j = 0; j < problem.operationCount; j++) {
                        operationTimes[j] = Integer.parseInt(operationTimesStrings[j][i]);
                    }
                    jobs[i] = operationTimes;
                }

                problem.jobs = jobs;

                parsedProblems.add(problem);
            }
        }
    }

    public long getInitialSeed() {
        return initialSeed;
    }

    public List<FlowshopProblem> getProblems() {
        return parsedProblems;
    }
}
