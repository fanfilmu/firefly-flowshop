package pl.agh.bo.flowshop.solver;

import pl.agh.bo.flowshop.solution.FlowshopSolution;

public interface SolverListener {
    public void onIterationFinished(int i, long best);

    public void onSolverFinished(FlowshopSolution bestSolution, double elapsedTime);
}
