package pl.agh.bo.flowshop.movement;

import pl.agh.bo.flowshop.problem.FlowshopProblem;
import pl.agh.bo.flowshop.solution.FlowshopSolution;
import pl.agh.bo.flowshop.solver.SolverParameters;

public interface MovementStrategy {
    /**
     * Moves first firefly closer to the second
     * @param first First firefly that is supposed to move
     * @param second Second firefly to which first one could move
     * @param problem Instance of the problem containing job definitions
     * @param params Parameters of the solver
     */
    void move(FlowshopSolution first, FlowshopSolution second, FlowshopProblem problem, SolverParameters params);
}
