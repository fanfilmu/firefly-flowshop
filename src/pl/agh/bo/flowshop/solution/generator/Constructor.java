package pl.agh.bo.flowshop.solution.generator;

import pl.agh.bo.flowshop.problem.FlowshopProblem;
import pl.agh.bo.flowshop.solution.FlowshopSolution;
import pl.agh.bo.flowshop.solution.FlowshopSolutionType;

public interface Constructor {
    public FlowshopSolution apply(FlowshopProblem problem, FlowshopSolutionType type);
}
