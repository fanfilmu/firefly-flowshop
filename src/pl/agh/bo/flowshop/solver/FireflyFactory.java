package pl.agh.bo.flowshop.solver;

import pl.agh.bo.flowshop.Firefly;
import pl.agh.bo.flowshop.Job;

import java.util.Random;

public class FireflyFactory {

    private Random random = new Random();

    private Job[] mJobs;
    private double mBaseAttraction;
    private double mLightAbsorption;

    public FireflyFactory(Job[] jobs, double baseAttraction, double lightAbsorption) {
        mJobs = jobs.clone();
        mBaseAttraction = baseAttraction;
        mLightAbsorption = lightAbsorption;
    }

    public Firefly spawnRandom() {
        shufflemJobs();

        return new Firefly(mJobs.clone(), mBaseAttraction, mLightAbsorption);
    }
    
    private void shufflemJobs() {
        Job temp;
        int index;

        for (int i = mJobs.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);

            if (index != i)
            {
                temp = mJobs[index];
                mJobs[index] = mJobs[i];
                mJobs[i] = temp;
            }
        }
    }
}
