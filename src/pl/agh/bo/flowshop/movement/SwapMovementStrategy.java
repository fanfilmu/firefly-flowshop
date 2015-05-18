package pl.agh.bo.flowshop.movement;

import pl.agh.bo.flowshop.problem.FlowshopProblem;
import pl.agh.bo.flowshop.solution.FlowshopSolution;
import pl.agh.bo.flowshop.solver.SolverParameters;

public class SwapMovementStrategy extends AbstractMovementStrategy {
    @Override
    public FlowshopSolution move(FlowshopSolution first, FlowshopSolution second, FlowshopProblem problem, SolverParameters params) {
        int currentDistance = calculateDistance(first, second);
        int swaps = getNumberOfSwaps(currentDistance, params);

        FlowshopSolution solution = first.clone();

        int index, requestedValue;
        while (swaps > 0) {
            index = random.nextInt(problem.jobCount);
            while (solution.get(index) == (requestedValue = second.get(index)))
                index = (index + 1) % problem.jobCount;

            solution.swap(indexOf(requestedValue, solution), index);

            if (calculateDistance(solution, second) <= 1)
                swaps = 0;
            else
                swaps--;
        }

        return solution;
    }

    private int indexOf(int requestedValue, FlowshopSolution first) {
        for (int i = 0; i < first.getLength(); i++)
            if (first.get(i) == requestedValue)
                return i;
        return -1;
    }

    private int getNumberOfSwaps(int distance, SolverParameters params)
    {
        float swapFraction = random.nextFloat();
        return Math.max(1, Math.round(swapFraction * distance / (2 - params.absorptionCoefficient)));
    }

}
