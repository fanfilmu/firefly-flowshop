package pl.agh.bo.flowshop.crossover;

import org.junit.Test;
import pl.agh.bo.flowshop.Firefly;
import pl.agh.bo.flowshop.Job;

import static org.junit.Assert.assertArrayEquals;

public class SwapOperatorTest
{
    @Test
    public void testApply() throws Exception
    {

        Job[] jobs = new Job[6];

        for (int i = 0; i < 6; i++)
            jobs[i] = new Job(i, new Integer[] { 1, 2, 3 });

        Firefly parentA = new Firefly(new Job[] { jobs[4], jobs[1], jobs[2], jobs[3], jobs[0], jobs[5] }, 1, 1);
        Firefly parentB = new Firefly(new Job[] { jobs[0], jobs[1], jobs[3], jobs[5], jobs[4], jobs[2] }, 1, 1);

        SwapOperator op = new SwapOperator(100045642, 1, 1);

        Job[] expectedChildA = new Job[] { jobs[4], jobs[1], jobs[2], jobs[5], jobs[0], jobs[3] };
        Job[] expectedChildB = new Job[] { jobs[0], jobs[1], jobs[3], jobs[2], jobs[4], jobs[5] };

        Firefly[] result = op.apply(parentA, parentB);

        assertArrayEquals(expectedChildA, result[0].getJobsDistribution());
        assertArrayEquals(expectedChildB, result[1].getJobsDistribution());
    }
}
