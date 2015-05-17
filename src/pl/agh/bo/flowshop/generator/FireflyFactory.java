package pl.agh.bo.flowshop.generator;

import pl.agh.bo.flowshop.Firefly;
import pl.agh.bo.flowshop.Job;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FireflyFactory {

    private Random random = new Random();

    private Job[] jobs;
    private double baseAttraction;
    private double lightAbsorption;

    public FireflyFactory(Job[] jobs, double baseAttraction, double lightAbsorption) {
        this.jobs = jobs.clone();
        this.baseAttraction = baseAttraction;
        this.lightAbsorption = lightAbsorption;
    }

    public Firefly spawnRandom() {
        shuffleJobs();

        return new Firefly(jobs.clone(), baseAttraction, lightAbsorption);
    }

    public Firefly spawn(ConstructorType constructorType) {
        Constructor constructor = null;

        switch (constructorType) {
            case NEH:
                break;
            default:
                constructor = new CdsConstructor(baseAttraction, lightAbsorption);
                break;
        }

        return constructor.apply(jobs);
    }

    public Map<Long, Firefly> spawnRandomPopulation(long size) {
        Map<Long, Firefly> population = new HashMap<Long, Firefly>();

        for (long i = 0; i < size - 1; i++) {
            population.put(i, spawnRandom());
        }

        return population;
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
