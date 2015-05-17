package pl.agh.bo.flowshop.solver;

import pl.agh.bo.flowshop.Evaluator.IEvaluator;
import pl.agh.bo.flowshop.Evaluator.MakespanEvaluator;
import pl.agh.bo.flowshop.Firefly;
import pl.agh.bo.flowshop.Job;
import pl.agh.bo.flowshop.crossover.CrossoverOperator;
import pl.agh.bo.flowshop.crossover.PmxOperator;
import pl.agh.bo.flowshop.crossover.SwapOperator;

import java.util.*;

/**
 * Created by Andrzej on 2015-04-21.
 */
public class Solver {

    private long mMaxIterations;
    private long mPopulationSize;
    private double mAbsorptionCoefficient;
    private double mLightAbsorption;
    private double mBaseAttraction;
    private String crossoverOperator;
    private IEvaluator evaluator;

    private Map<Long, Firefly> fireflies;

    private Job[] mJobs;

    public Solver(Job[] jobs, long maxIterations, long populationSize, double absorptionCoefficient, double lightAbsorption,
                  double baseAttraction, String crossoverOperator) {
        mJobs = jobs;
        mMaxIterations = maxIterations;
        mPopulationSize = populationSize;
        mAbsorptionCoefficient = absorptionCoefficient;
        mLightAbsorption = lightAbsorption;
        mBaseAttraction = baseAttraction;
        this.crossoverOperator = crossoverOperator;
        evaluator = new MakespanEvaluator();
    }

    /**
     * Pseudcode
     *
     * 1. Generate fireflies (based on a list of jobs acquired from InputParser).
     *      Mark one as the initial one
     * 2. Calculate light intensity for every firefly using MakespanEvaluator
     * 3. while (i < mMaxIterations)
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
        System.out.format("Generating fireflies...%n");
        fireflies = generateFireflies(mPopulationSize);

        System.out.format("Assigning initial light intensity...%n");
        fireflies = recalculateIntensity(fireflies);

        CrossoverOperator crossOp;

        if (crossoverOperator.equals("swap")) {
            crossOp = new SwapOperator(mBaseAttraction, mLightAbsorption);
        } else {
            crossOp = new PmxOperator(initialSeed, mBaseAttraction, mLightAbsorption);
        }
        Firefly[] children;

        while (i++ < mMaxIterations) {
            if (i % 100 == 0) System.out.format("%d of %d%n", i, mMaxIterations);

            for (Firefly fireflyA : fireflies.values()) {
                for (Firefly fireflyB : fireflies.values()) {
                    if (fireflyA.equals(fireflyB)) continue;

                    // jezeli B jest gorszy niz A, przenosimy B w lepsze miejsce
                    if (fireflyB.getLightIntensity() > fireflyA.getLightIntensity()) {
                        children = crossOp.apply(fireflyA, fireflyB);
                        evaluator.setJobs(Arrays.asList(children[0].getJobsDistribution()));
                        long firstChild = evaluator.evaluate();
                        evaluator.setJobs(Arrays.asList(children[1].getJobsDistribution()));
                        long secondChild = evaluator.evaluate();

                        if (firstChild < fireflyB.getLightIntensity() && firstChild < secondChild) {
                            fireflyB.setJobsDistribution(children[0].getJobsDistribution());
                            fireflyB.setLightIntensity(firstChild);
                        } else if (secondChild < fireflyB.getLightIntensity() && secondChild < firstChild) {
                            fireflyB.setJobsDistribution(children[1].getJobsDistribution());
                            fireflyB.setLightIntensity(secondChild);
                        }
                    }
                }
            }

            // Recalculate light intensity for every firefly
            fireflies = recalculateIntensity(fireflies);

            // Find the best firefly
            bestOne = findBest(fireflies);

            if (new Random().nextDouble() < 0.1)
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
        for (Firefly firefly : fireflies.values()) {
            evaluator.setJobs(Arrays.asList(firefly.getJobsDistribution()));
            firefly.setLightIntensity(evaluator.evaluate());
        }

        return fireflies;
    }

    private Map<Long, Firefly> generateFireflies(long populationSize) {
        Map<Long, Firefly> fireflies = new HashMap<Long, Firefly>();

        FireflyFactory fireflyFactory = new FireflyFactory(mJobs, mBaseAttraction, mLightAbsorption);

        for (long i = 0; i < populationSize; i++) {
            fireflies.put(i, fireflyFactory.spawnRandom());
        }

        return fireflies;
    }
}
