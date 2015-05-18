package pl.agh.bo.flowshop.generator.neh;

import org.junit.Test;
import pl.agh.bo.flowshop.generator.cds.CDSConstructor;
import pl.agh.bo.flowshop.problem.FlowshopProblem;
import pl.agh.bo.flowshop.solution.FlowshopSolution;
import pl.agh.bo.flowshop.solution.SolutionFactory;

import static org.junit.Assert.*;

public class NEHConstructorTest {

    @Test
    public void testApply() throws Exception {
        NEHConstructor constructor = new NEHConstructor(-1);
        FlowshopProblem problem = new FlowshopProblem();
        problem.jobCount = 5;
        problem.operationCount = 5;
        problem.jobs = new int[5][];
        problem.jobs[0] = new int[] { 5, 9, 8, 10, 1 };
        problem.jobs[1] = new int[] { 9, 3, 10, 1, 8 };
        problem.jobs[2] = new int[] { 9, 4, 5, 8, 6 };
        problem.jobs[3] = new int[] { 4, 8, 8, 7, 2 };
        problem.jobs[4] = new int[] { 3, 5, 6, 3, 7 };

        FlowshopSolution solution = constructor.apply(problem, SolutionFactory.SolutionType.VECTOR);
        assertArrayEquals(new int[] { 4, 3, 1, 2, 0 }, solution.getOrder());

        constructor = new NEHConstructor(4);
        solution = constructor.apply(problem, SolutionFactory.SolutionType.VECTOR);
        assertArrayEquals(new int[] { 3, 1, 2, 0, 4 }, solution.getOrder());
    }
}
