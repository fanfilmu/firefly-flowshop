package pl.agh.bo.flowshop.solution.generator;

import org.junit.Test;
import pl.agh.bo.flowshop.problem.FlowshopProblem;
import pl.agh.bo.flowshop.solution.FlowshopSolution;
import pl.agh.bo.flowshop.solution.FlowshopSolutionType;
import pl.agh.bo.flowshop.solution.SolutionFactory;

import static org.junit.Assert.*;

public class CDSConstructorTest {

    @Test
    public void testApply() throws Exception {
        CDSConstructor constructor = new CDSConstructor();
        FlowshopProblem problem = new FlowshopProblem();
        problem.jobCount = 4;
        problem.operationCount = 3;
        problem.jobs = new int[4][];
        problem.jobs[0] = new int[] { 6, 5, 4 };
        problem.jobs[1] = new int[] { 8, 1, 4 };
        problem.jobs[2] = new int[] { 3, 5, 4 };
        problem.jobs[3] = new int[] { 4, 4, 2 };

        FlowshopSolution solution = constructor.apply(problem, FlowshopSolutionType.VECTOR);
        assertArrayEquals(new int[]{2, 0, 3, 1}, solution.getOrder());
    }
}
