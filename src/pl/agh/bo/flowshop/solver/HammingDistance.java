package pl.agh.bo.flowshop.solver;

import pl.agh.bo.flowshop.solution.FlowshopSolution;

public class HammingDistance {
    public static int calculateDistance(FlowshopSolution solution1, FlowshopSolution solution2) {
        if (solution1.getLength() != solution2.getLength())
            throw new IllegalArgumentException("Lengths of the vectors must be equal");

        int dist = 0;
        for (int i = 0; i < solution1.getLength(); i++)
            if (solution1.get(i) != solution2.get(i))
                dist += 1;

        return dist;
    }
}
