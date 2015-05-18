package pl.agh.bo.flowshop.movement;

import org.junit.Test;
import pl.agh.bo.flowshop.InputParser;
import pl.agh.bo.flowshop.problem.FlowshopProblem;
import pl.agh.bo.flowshop.solution.FlowshopSolution;
import pl.agh.bo.flowshop.solution.FlowshopSolutionType;
import pl.agh.bo.flowshop.solution.SolutionFactory;
import pl.agh.bo.flowshop.solver.HammingDistance;
import pl.agh.bo.flowshop.solver.SolverParameters;

import static org.junit.Assert.*;

public class SwapMovementStrategyTest {

    @Test
    public void testMove() throws Exception {
        InputParser parser = new InputParser("test.txt");
        parser.parse();
        FlowshopProblem problem = parser.getProblems().get(0);
        SolverParameters params = new SolverParameters();

        SolutionFactory factory = new SolutionFactory(problem, FlowshopSolutionType.VECTOR, SolutionFactory.GeneratorType.RANDOM);
        FlowshopSolution solution1 =  factory.spawn();
        FlowshopSolution solution2 =  factory.spawn();

        SwapMovementStrategy strategy = new SwapMovementStrategy();
        FlowshopSolution result = strategy.move(solution1, solution2, problem, params);

        System.out.println("Solution 1:");
        for (int i = 0; i < problem.jobCount; i++)
            System.out.format("%2d ", solution1.get(i));

        System.out.format("%nSolution 2:%n");
        for (int i = 0; i < problem.jobCount; i++)
            System.out.format("%2d ", solution2.get(i));

        System.out.format("%nResult:%n");
        for (int i = 0; i < problem.jobCount; i++)
            System.out.format("%2d ", result.get(i));

        System.out.format("%nDist 1-2: %2d; Dist res-2: %2d%n",
                HammingDistance.calculateDistance(solution1, solution2),
                HammingDistance.calculateDistance(result, solution2));

        assertTrue(true);
    }
}