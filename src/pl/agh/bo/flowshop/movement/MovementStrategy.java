package pl.agh.bo.flowshop.movement;

import pl.agh.bo.flowshop.problem.FlowshopProblem;
import pl.agh.bo.flowshop.solution.FlowshopSolution;
import pl.agh.bo.flowshop.solver.SolverParameters;

public interface MovementStrategy {
    /**
     * Determines where, if anywhere, first firefly should move for given problem and solver parameters
     * @param first First firefly that is supposed to move
     * @param second Second firefly to which first one could move
     * @param problem Instance of the problem containing job definitions
     * @param params Parameters of the solver
     * @return New order of jobs for first firefly or null, if it should not move at all
     */
    FlowshopSolution move(FlowshopSolution first, FlowshopSolution second, FlowshopProblem problem, SolverParameters params);
}
