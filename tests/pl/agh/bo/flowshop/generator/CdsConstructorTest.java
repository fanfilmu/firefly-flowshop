package pl.agh.bo.flowshop.generator;

import org.junit.Test;
import pl.agh.bo.flowshop.Job;

import static org.junit.Assert.*;

public class CdsConstructorTest {

    @Test
    public void testApply() throws Exception {
        CdsConstructor constructor = new CdsConstructor();
        Job[] jobs = new Job[] {
                new Job(0, new Integer[] { 6, 5, 4 }),
                new Job(1, new Integer[] { 8, 1, 4 }),
                new Job(2, new Integer[] { 3, 5, 4 }),
                new Job(3, new Integer[] { 4, 4, 2 })
        };

        Job[] result = constructor.apply(jobs).getJobsDistribution();
        assertArrayEquals(new int[]{2, 0, 3, 1}, new int[] {
                result[0].getId(), result[1].getId(), result[2].getId(), result[3].getId()
        });
    }
}
