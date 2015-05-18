package pl.agh.bo.flowshop.solver;

import pl.agh.bo.flowshop.GUI.ResultsChart;
import pl.agh.bo.flowshop.evaluator.IEvaluator;
import pl.agh.bo.flowshop.evaluator.MakespanEvaluator;
import pl.agh.bo.flowshop.Firefly;
import pl.agh.bo.flowshop.Job;
import pl.agh.bo.flowshop.crossover.CrossoverOperator;
import pl.agh.bo.flowshop.crossover.PmxOperator;
import pl.agh.bo.flowshop.crossover.SwapOperator;
import pl.agh.bo.flowshop.generator.ConstructorType;
import pl.agh.bo.flowshop.generator.FireflyFactory;

import java.util.*;

/**
 * Created by Andrzej on 2015-04-21.
 */
public class Solver {

    private long maxIterations;
    private long populationSize;

    private double lightAbsorption;
    private double baseAttraction;

    private String crossoverOperator;

    private boolean cds;
    private boolean neh;

    private IEvaluator evaluator;

    private Map<Long, Firefly> fireflies;

    private Job[] jobs;

    private ResultsChart resultsChart;

    public Solver(Job[] jobs, long maxIterations, long populationSize, double lightAbsorption,
                  double baseAttraction, String crossoverOperator, boolean cds, boolean neh) {
        // TODO: What about absorption coefficient?

        this.jobs = jobs;

        this.maxIterations = maxIterations;
        this.populationSize = populationSize;

        this.lightAbsorption = lightAbsorption;
        this.baseAttraction = baseAttraction;

        this.crossoverOperator = crossoverOperator;

        this.cds = cds;
        this.neh = neh;

        this.evaluator = new MakespanEvaluator();
    }

    public Firefly run(long initialSeed, ResultsChart resultsChart) {
        int i = 0;
        Firefly bestOne = null;
        this.resultsChart = resultsChart;

        // Start measuring time of algorithm
        long startTime = System.nanoTime();

        System.out.format("Generating fireflies...%n");

        fireflies = generateInitialPopulation();

        System.out.format("Assigning initial light intensity...%n");
        fireflies = recalculateIntensity(fireflies);

        CrossoverOperator crossOp;

        if (crossoverOperator.equals("swap")) {
            crossOp = new SwapOperator(baseAttraction, lightAbsorption);
        } else {
            crossOp = new PmxOperator(initialSeed, baseAttraction, lightAbsorption);
        }
        Firefly[] children;

        while (i++ < maxIterations) {
            if (i % 100 == 0) System.out.format("%d of %d%n", i, maxIterations);

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

            // Tell about it ResultsChart
            resultsChart.addResult(i, bestOne.getLightIntensity());

            if (new Random().nextDouble() < 0.1)
                bestOne = moveRandomly(bestOne);
        }


        // Stop measuring time of algorithm and supply it to results form
        long stopTime = System.nanoTime();
        double elapsedTime = (stopTime - startTime)/(1000000000.0);

        // Show the user results
        resultsChart.showResults(bestOne, elapsedTime);

        return bestOne;

    }

    private Map<Long, Firefly> generateInitialPopulation() {
        FireflyFactory fireflyFactory = new FireflyFactory(jobs, baseAttraction, lightAbsorption);

        Map<Long, Firefly> population = fireflyFactory.spawnRandomPopulation(populationSize);

        long i = population.size();

        if (cds) {
            population.put(i++, fireflyFactory.spawn(ConstructorType.CDS));
        }

        if (neh) {
            population.put(i++, fireflyFactory.spawn(ConstructorType.NEH));
        }

        return population;
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
}
