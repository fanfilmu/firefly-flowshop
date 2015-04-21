package pl.agh.bo.flowshop;

import pl.agh.bo.flowshop.Evaluator.MakespanEvaluator;
import pl.agh.bo.flowshop.solver.Solver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by fan on 20.04.15.
 */
public class Main {
    public static void main(String args[]) {
        System.out.println("Dobry.");
        List<Job> jobs;

        try {
            InputParser parser = new InputParser("firefly-flowshop/test.txt");
            parser.parse();

            System.out.println("Initial seed: " + parser.getInitialSeed());
            System.out.println("Jobs:");
            jobs = parser.getJobs();
            for (Job job: jobs)
                System.out.println(job);
            MakespanEvaluator ev = new MakespanEvaluator();
            ev.setJobs(jobs);
            System.out.println("\nThis needs time equals to: " + ev.evaluate());

            Solver algo = new Solver(jobs.toArray(new Job[20]));
            Firefly result = algo.run(20);

            for (Job job: result.getJobsDistribution())
                System.out.println(job);

            ev.setJobs(Arrays.asList(result.getJobsDistribution()));
            System.out.println("\nThis needs time equals to: " + ev.evaluate());

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
