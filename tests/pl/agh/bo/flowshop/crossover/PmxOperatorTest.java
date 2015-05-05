package pl.agh.bo.flowshop.crossover;

import org.junit.Test;
import pl.agh.bo.flowshop.Firefly;
import pl.agh.bo.flowshop.Job;

import static org.junit.Assert.*;

public class PmxOperatorTest {

    @Test
    public void testApply() throws Exception {
        Job[] jobs = new Job[8];
        for (int i = 0; i < 8; i++)
            jobs[i] = new Job(i, new Integer[] { 1, 2, 3 });

        Firefly parent1 = new Firefly(new Job[] { jobs[0], jobs[1], jobs[2], jobs[3], jobs[4], jobs[5], jobs[6], jobs[7] }, 1, 1);
        Firefly parent2 = new Firefly(new Job[] { jobs[3], jobs[6], jobs[1], jobs[0], jobs[7], jobs[2], jobs[5], jobs[4] }, 1, 1);

        PmxOperator operator = new PmxOperator(16516L, 1, 1);
        Firefly[] result = operator.apply(parent1, parent2);

        int requiredValues1 = 0;
        int requiredValues2 = 0;

        Job[] result0 = result[0].getJobsDistribution();
        Job[] result1 = result[1].getJobsDistribution();

        for (int i = 0; i < 8; i++) {
            Integer val1 = result0[i].getId();
            Integer val2 = result1[i].getId();

            System.out.format("%d | %d%n", val1, val2);

            requiredValues1 = requiredValues1 | (1 << val1);
            requiredValues2 = requiredValues2 | (1 << val2);
        }

        assertEquals((1 << 8) - 1, requiredValues1);
        assertEquals((1 << 8) - 1, requiredValues2);
    }
}