package pl.agh.bo.flowshop;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrzej on 2015-04-20.
 */
public class InputParser {

    private File file;
    private BufferedReader reader;
    private long initialSeed;
    private List<Job> jobs;
    private String[][] operationTimesStrings;

    public InputParser(String fileName) throws FileNotFoundException {
        // Get the file handler
        String path = new File(".").getAbsolutePath();
        file = new File(fileName);
        if (!file.exists()) throw new FileNotFoundException("Podaj ścieżkę do poprawnego pliku ;)");

        // Get file reader
        reader = new BufferedReader(new FileReader(fileName));

        // Initialize list of jobs
        jobs = new ArrayList<Job>();
    }

    /**
     * Parse the file. Get collections of jobs and initial seed
     */
    public void parse() throws IOException {
        // Read first line. It's not important
        reader.readLine();
        String[] data = reader.readLine().split(" ");

        // Get number of jobs
        int jobsAmount = Integer.valueOf(data[0]);

        // Get number of operations (easier to iterate over file)
        int machineAmount = Integer.valueOf(data[1]);

        // Get initial seed
        initialSeed = Long.valueOf(data[2]);

        // Initialize array of operation times
        operationTimesStrings = new String[machineAmount][jobsAmount];

        // Read another title line
        reader.readLine();

        // Build operationTimesStrings array
        for (int i=0 ; i < machineAmount; i++) {
            // Read line and fill the array
            operationTimesStrings[i] = reader.readLine().split(" ");
        }

        // Build collection of jobs
        for (int i=0 ; i < jobsAmount; i++) {
            Integer[] operationTimes = new Integer[machineAmount];
            for (int j=0 ; j < machineAmount ; j++) {
                operationTimes[j] = Integer.parseInt(operationTimesStrings[j][i]);
            }
            jobs.add(new Job(i+1, operationTimes));
        }

    }

    public long getInitialSeed() {
        return initialSeed;
    }

    public List<Job> getJobs() {
        return jobs;
    }
}
