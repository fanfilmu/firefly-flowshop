package pl.agh.bo.flowshop.problem;

import pl.agh.bo.flowshop.evaluator.IEvaluator;
import pl.agh.bo.flowshop.evaluator.MakespanEvaluator;
import pl.agh.bo.flowshop.solution.FlowshopSolution;

/**
 * This structure-like class contains information about an instance of a Flowhop problem
 * Apart from information about jobs and operation times, it also can contain information
 * about initial seed and bounds
 */
public class FlowshopProblem {
    public int jobCount, operationCount, lowerBound, upperBound;
    public long initialSeed;
    public int[][] jobs;
    private IEvaluator evaluator = new MakespanEvaluator();

    public long evaluateSolution(FlowshopSolution solution) {
        int[][] newJobs = new int[jobCount][];
        for (int i = 0; i < jobCount; i++)
            newJobs[i] = jobs[solution.get(i)];

        evaluator.setJobs(newJobs);
        return evaluator.evaluate();
    }
}
