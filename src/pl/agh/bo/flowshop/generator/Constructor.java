package pl.agh.bo.flowshop.generator;

import pl.agh.bo.flowshop.problem.FlowshopProblem;
import pl.agh.bo.flowshop.solution.FlowshopSolution;
import pl.agh.bo.flowshop.solution.SolutionFactory;

public interface Constructor {
    public FlowshopSolution apply(FlowshopProblem problem, SolutionFactory.SolutionType type);
}
