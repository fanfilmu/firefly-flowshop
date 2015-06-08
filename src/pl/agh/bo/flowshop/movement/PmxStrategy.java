package pl.agh.bo.flowshop.movement;

import pl.agh.bo.flowshop.problem.FlowshopProblem;
import pl.agh.bo.flowshop.solution.FlowshopSolution;
import pl.agh.bo.flowshop.solver.SolverParameters;

public class PmxStrategy extends AbstractMovementStrategy {
    // http://www.rubicite.com/Tutorials/GeneticAlgorithms/CrossoverOperators/PMXCrossoverOperator.aspx
    @Override
    public void move(FlowshopSolution first, FlowshopSolution second, FlowshopProblem problem, SolverParameters params) {
        int currentDistance = calculateDistance(first, second);

        int cuttingPoint = random.nextInt(problem.jobCount);
        int swathSize = calculateSwathSize(currentDistance, problem.jobCount, params);

        // copy swath
        for (int i = cuttingPoint; swathSize > 0; swathSize--) {
            first.swap(i, findIndex(first.getOrder(), second.get(i)));
            i = (i + 1) % problem.jobCount;
        }
    }

    private int calculateSwathSize(int currentDistance, int jobCount, SolverParameters params) {
        int size = jobCount - Math.round((1 - params.baseAttraction) * currentDistance); // new firefly should be closer to the better one
        size += random.nextInt(Math.max(1, (int)(currentDistance / (2 - params.absorptionCoefficient))));

        return size;
    }

    private int findIndex(int[] order, int value) {
        for (int i = 0; i < order.length; i++)
            if (order[i] == value) return i;
        return -1;
    }
}
