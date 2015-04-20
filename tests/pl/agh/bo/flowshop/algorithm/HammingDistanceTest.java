package pl.agh.bo.flowshop.algorithm;

import org.junit.Test;
import pl.agh.bo.flowshop.Job;

import static org.junit.Assert.*;

public class HammingDistanceTest {

    @Test
    public void testCalculateDistance() throws Exception {
        Job[][] sets = getSets();

        assertEquals(5, HammingDistance.calculateDistance(sets[0], sets[1]).intValue());
        assertEquals(5, HammingDistance.calculateDistance(sets[0], sets[2]).intValue());
        assertEquals(6, HammingDistance.calculateDistance(sets[1], sets[2]).intValue());
    }

    private Job[][] getSets() {
        Job[] jobs = new Job[8];
        for (int i = 0; i < 8; i++)
            jobs[i] = new Job(i, new Integer[] { 1, 2, 3 });
        
        Job[][] result = new Job[8][2];
        result[0] = new Job[] { jobs[0], jobs[1], jobs[2], jobs[3], jobs[4], jobs[5], jobs[6], jobs[7] };
        result[1] = new Job[] { jobs[0], jobs[7], jobs[2], jobs[5], jobs[3], jobs[4], jobs[6], jobs[1] };
        result[2] = new Job[] { jobs[1], jobs[7], jobs[2], jobs[3], jobs[4], jobs[6], jobs[5], jobs[0] };

        return result;
    }
}
