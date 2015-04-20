package pl.agh.bo.flowshop.algorithm;

import pl.agh.bo.flowshop.Firefly;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrzej on 2015-04-21.
 */
public class Algorithm {

    private final static long MAX_ITERATIONS = 2000;

    private Map<Firefly, Long> fireflies;


    /**
     * Pseudcode
     *
     * 1. Generate fireflies (based on a list of jobs acquired from InputParser).
     *      Mark one as the initial one
     * 2. Calculate light intensity for every firefly using Evaluator
     * 3. while (i < max_iterations)
     *      for (every firefly_a)
     *          for (every firefly_b)
     *              if (light_absorb(firefly_a) > light_absord(firefly_b))
     *                  swap firefly_a vector randomly ^^
     * 4. Recalculate light intensity for every firefly
     * 5. Move randomly the best firefly ?? (that's actually not ideal right now)
     */

    public void start(long initialSeed) {
        int i = 0;
        Firefly bestOne;

        // Generate fireflies based on initial seed (calculate their light intensity as well)
        fireflies = generateFireflies(initialSeed);

        while (i++ < MAX_ITERATIONS) {
            for (Firefly fireflyA : fireflies.keySet()) {
                for (Firefly fireflyB : fireflies.keySet()) {
                    if (!fireflyA.equals(fireflyB)) {
                        // Check light intensity and move fireflies
                        // using PMX crossing
                        // TODO: Use PMX here to move fireflies
                    }
                }
            }

            // Recalculate light intensity for every firefly
            fireflies = recalculateIntensity(fireflies);

            // Find the best firefly
            bestOne = findBest(fireflies);

            bestOne = moveRandomly(bestOne);
        }


    }

    private Firefly moveRandomly(Firefly bestOne) {
        return null;
    }

    private Firefly findBest(Map<Firefly, Long> fireflies) {
        return null;
    }

    private Map<Firefly, Long> recalculateIntensity(Map<Firefly, Long> fireflies) {
        return null;
    }

    private Map<Firefly, Long> generateFireflies(long initialSeed) {
        return null;
    }
}
