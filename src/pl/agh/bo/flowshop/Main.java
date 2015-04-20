package pl.agh.bo.flowshop;

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

        Firefly firefly1 = new Firefly(new int[]{2, 1, 4, 5, 3}, 2.0, 1.0);
        Firefly firefly2 = new Firefly(new int[]{1, 2, 4, 3, 5}, 2.0, 1.0);

        System.out.println(firefly1.getAttractiveness(firefly2));
    }
}
