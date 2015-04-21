package pl.agh.bo.flowshop.solver;

import pl.agh.bo.flowshop.Firefly;
import pl.agh.bo.flowshop.Job;

import java.util.Random;

public class FireflyFactory {

    private Random random = new Random();

    private Job[] jobs;

    public FireflyFactory(Job[] jobs) {
        this.jobs = jobs;
    }

    public Firefly spawnRandom() {
        shuffleJobs();

        return new Firefly(jobs);
    }

    private void shuffleJobs() {
        Job temp;
        int index;

        for (int i = jobs.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);

            if (index != i)
            {
                temp = jobs[index];
                jobs[index] = jobs[i];
                jobs[i] = temp;
            }
        }
    }
}
