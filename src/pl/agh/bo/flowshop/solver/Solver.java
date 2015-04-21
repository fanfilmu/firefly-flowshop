package pl.agh.bo.flowshop.solver;

import pl.agh.bo.flowshop.Evaluator;
import pl.agh.bo.flowshop.Firefly;
import pl.agh.bo.flowshop.Job;
import pl.agh.bo.flowshop.crossover.PmxOperator;

import java.util.*;

/**
 * Created by Andrzej on 2015-04-21.
 */
public class Solver {

    private final static long MAX_ITERATIONS = 2000;

    private final static long POPULATION_SIZE= 100;

    private final static double ABSORPTION_COEFFICIENT = 0.4;

    private Map<Long, Firefly> fireflies;

    private Job[] jobs;

    public Solver(Job[] jobs) {
        this.jobs = jobs;
    }

    /**
     * Pseudcode
     *
     * 1. Generate fireflies (based on a list of jobs acquired from InputParser).
     *      Mark one as the initial one
     * 2. Calculate light intensity for every firefly using Evaluator
     * 3. while (i < max_iterations)
     *      for (every firefly_a)
     *          for (every firefly_b)
     *              if (light_absorb(firefly_a) > light_absord(firefly_b))
     *                  swap firefly_a vector randomly ^^
     * 4. Recalculate light intensity for every firefly
     * 5. Move randomly the best firefly ?? (that's actually not ideal right now)
     */

    public Firefly run(long initialSeed) {
        int i = 0;
        Firefly bestOne = null;

        // Generate fireflies based on initial seed (calculate their light intensity as well)
        fireflies = generateFireflies(POPULATION_SIZE);

        fireflies = recalculateIntensity(fireflies);

        PmxOperator pmx = new PmxOperator(initialSeed, ABSORPTION_COEFFICIENT);

        while (i++ < MAX_ITERATIONS) {
            for (Firefly fireflyA : fireflies.values())
            {
                for (Firefly fireflyB : fireflies.values())
                {
                    if (!fireflyA.equals(fireflyB))
                    {
                        if (fireflyA.getLightIntensity() > fireflyB.getLightIntensity()) {
                            Firefly[] children = pmx.apply(fireflyA, fireflyB);

                            fireflyA.setJobsDistribution(children[0].getJobsDistribution());
                            fireflyB.setJobsDistribution(children[1].getJobsDistribution());
                        }
                    }
                }
            }

            // Recalculate light intensity for every firefly
            fireflies = recalculateIntensity(fireflies);

            // Find the best firefly
            bestOne = findBest(fireflies);

            bestOne = moveRandomly(bestOne);
        }

        return bestOne;

    }

    private Firefly moveRandomly(Firefly bestOne) {

        Job[] jobs = bestOne.getJobsDistribution();

        Random random = new Random();
        int i = random.nextInt(jobs.length);
        int j = random.nextInt(jobs.length);

        Job tmp = jobs[i];
        jobs[i] = jobs[j];
        jobs[j] = tmp;

        bestOne.setJobsDistribution(jobs);

        return bestOne;
    }

    private Firefly findBest(Map<Long, Firefly> fireflies) {
        long bestIntensity = Long.MAX_VALUE;
        Firefly result = null;

        for (Firefly firefly : fireflies.values()) {
            if (firefly.getLightIntensity() < bestIntensity) {
                bestIntensity = firefly.getLightIntensity();
                result = firefly;
            }
        }

        return result;
    }

    private Map<Long, Firefly> recalculateIntensity(Map<Long, Firefly> fireflies) {

        for (Firefly firefly : fireflies.values())
            firefly.setLightIntensity(new Evaluator(Arrays.asList(firefly.getJobsDistribution())).evaluate());

        return fireflies;
    }

    private Map<Long, Firefly> generateFireflies(long populationSize) {
        Map<Long, Firefly> fireflies = new HashMap<Long, Firefly>();

        FireflyFactory fireflyFactory = new FireflyFactory(jobs);

        for (long i = 0; i < populationSize; i++) {
            fireflies.put(i, fireflyFactory.spawnRandom());
        }

        return fireflies;
    }
}