package pl.agh.bo.flowshop.algorithm;

import org.junit.Test;

import static org.junit.Assert.*;

public class HammingDistanceTest {

    @Test
    public void testCalculateDistance() throws Exception {
        int[][] sets = getSets();

        assertEquals(5, HammingDistance.calculateDistance(sets[0], sets[1]).intValue());
        assertEquals(5, HammingDistance.calculateDistance(sets[0], sets[2]).intValue());
        assertEquals(6, HammingDistance.calculateDistance(sets[1], sets[2]).intValue());
    }

    private int[][] getSets() {
        int[][] result = new int[8][2];
        result[0] = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
        result[1] = new int[] { 0, 7, 2, 5, 3, 4, 6, 1 };
        result[2] = new int[] { 1, 7, 2, 3, 4, 6, 5, 0 };

        return result;
    }
}
