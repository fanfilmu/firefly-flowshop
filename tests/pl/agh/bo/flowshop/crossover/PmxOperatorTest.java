package pl.agh.bo.flowshop.crossover;

import org.junit.Test;
import pl.agh.bo.flowshop.Firefly;

import static org.junit.Assert.*;

public class PmxOperatorTest {

    @Test
    public void testApply() throws Exception {
        Firefly parent1 = new Firefly(new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, 0.5, 0.5);
        Firefly parent2 = new Firefly(new int[] { 3, 6, 1, 0, 7, 2, 5, 4 }, 0.5, 0.5);

        PmxOperator operator = new PmxOperator();
        Firefly[] result = operator.apply(parent1,parent2);

        int requiredValues1 = 0;
        int requiredValues2 = 0;

        int[] result0 = result[0].getJobsDistribution();
        int[] result1 = result[1].getJobsDistribution();

        for (int i = 0; i < 8; i++) {
            Integer val1 = result0[i];
            Integer val2 = result1[i];

            System.out.format("%d | %d%n", val1, val2);

            requiredValues1 = requiredValues1 | (1 << val1);
            requiredValues2 = requiredValues2 | (1 << val2);
        }

        assertEquals((1 << 8) - 1, requiredValues1);
        assertEquals((1 << 8) - 1, requiredValues2);
    }
}