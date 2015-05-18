package pl.agh.bo.flowshop.problem;

import pl.agh.bo.flowshop.evaluator.IEvaluator;
import pl.agh.bo.flowshop.evaluator.MakespanEvaluator;

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

    public long evaluateSolution(int[] order) {
        int[][] solution = new int[order.length][];
        for (int i = 0; i < order.length; i++)
            solution[i] = jobs[order[i]];

        evaluator.setJobs(solution);
        return evaluator.evaluate();
    }
}
