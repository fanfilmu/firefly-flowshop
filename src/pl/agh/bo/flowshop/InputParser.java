package pl.agh.bo.flowshop;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrzej on 2015-04-20.
 */
public class InputParser {

    private final String filename;
    long initialSeed;
    private List<List<Job>> parsedJobs;

    public InputParser(String filename) throws FileNotFoundException {
        // Get the file handler
        File file = new File(filename);
        if (!file.exists()) throw new FileNotFoundException("Podaj ścieżkę do poprawnego pliku ;)");

        this.filename = filename;
        this.parsedJobs = new LinkedList<>();
    }

    /**
     * Parse the file. Get collections of jobs and initial seed
     */
    public void parse() throws IOException {
        String line;
        String params[];
        String[][] operationTimesStrings;
        int jobsAmount, machinesAmount, upperBound, lowerBound;
        Integer operationTimes[];
        long initialSeed;
        List<Job> jobs;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // new problem definition - first line has no meaning
            while (reader.readLine() != null) {
                jobs = new LinkedList<Job>();

                // global problem parameters expected, e.g.
                // 100 5 896678084 5495 5437
                line = reader.readLine();
                params = line.trim().split("[ ]+");

                jobsAmount = Integer.parseInt(params[0]);
                machinesAmount = Integer.parseInt(params[1]);
                initialSeed = Long.parseLong(params[2]); //TODO add initial seed and bounds to problem definition
                upperBound = Integer.parseInt(params[3]);
                lowerBound = Integer.parseInt(params[4]);
                operationTimesStrings = new String[machinesAmount][jobsAmount];

                // skip "processing times" caption
                reader.readLine();


                for (int i = 0; i < machinesAmount; i++) {
                    // read operation costs
                    operationTimesStrings[i] = reader.readLine().trim().split("[ ]+");
                }

                // build collection of jobs
                for (int i = 0; i < jobsAmount; i++) {
                    operationTimes = new Integer[machinesAmount];
                    for (int j = 0; j < machinesAmount; j++) {
                        operationTimes[j] = Integer.parseInt(operationTimesStrings[j][i]);
                    }
                    jobs.add(new Job(i, operationTimes));
                }

                parsedJobs.add(jobs);
            }
        }
    }

    public long getInitialSeed() {
        return initialSeed;
    }

    public List<List<Job>> getJobs() {
        return parsedJobs;
    }
}
