package pl.agh.bo.flowshop.algorithm;

/**
 * Created by Andrzej on 2015-04-21.
 */
public class Algorithm {

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
}
