package pl.agh.bo.flowshop;

import pl.agh.bo.flowshop.solver.Solver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by fan on 20.04.15.
 */
public class Main {
    public static void main(String args[]) {
        System.out.println("Dobry.");

        try {
            InputParser parser = new InputParser("firefly-flowshop\\test.txt");
            parser.parse();

            System.out.println("Initial seed: " + parser.getInitialSeed());
            System.out.println("Jobs:");
            List<Job> jobs = parser.getJobs();
            for (Job job: jobs)
                System.out.println(job);
            Evaluator ev = new Evaluator(jobs);
            System.out.println("\nThis needs time equals to: " + ev.evaluate());

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Job[] jobs = new Job[2];

        jobs[0] = new Job(0, new Integer[] { 20, 10 });
        jobs[1] = new Job(1, new Integer[] { 10, 20 });

        Solver algo = new Solver(jobs);

        algo.run(20);
    }
}
