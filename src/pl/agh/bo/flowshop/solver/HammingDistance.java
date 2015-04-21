package pl.agh.bo.flowshop.solver;

import pl.agh.bo.flowshop.Firefly;
import pl.agh.bo.flowshop.Job;

public class HammingDistance {
    public static Integer calculateDistance(Firefly firefly1, Firefly firefly2) {
        return calculateDistance(firefly1.getJobsDistribution(), firefly2.getJobsDistribution());
    }

    public static Integer calculateDistance(Job[] firefly1, Job[] firefly2) {
        if (firefly1.length != firefly2.length)
            throw new IllegalArgumentException("Lengths of the vectors must be equal");

        int dist = 0;
        for (int i = 0; i < firefly1.length; i++) {
            if (!firefly1[i].equals(firefly2[i])) dist += 1;
        }

        return dist;
    }
}
