package pl.agh.bo.flowshop.crossover;

import pl.agh.bo.flowshop.Firefly;
import pl.agh.bo.flowshop.Job;

import java.util.*;

/**
 * Operator krzyżowania PMX
 * Konstruktor pobiera *seed* (może być null) oraz absorptionCoefficient, będący parametrem
 * informującym jak bardzo dziecko powinno być podobne do jednego z rodziców.
 *
 * Metoda apply zwraca dwoje dzieci: pierwsze, podobne (w stopniu zależnym od absorptionCoefficient,
 * im większy tym bardziej podobne) do pierwszego rodzica i drugie, podobne do drugiego rodzica.
 */
public class PmxOperator implements CrossoverOperator {
    private Long seed = null;
    private double lightAbsorption;
    private double baseAttraction;
    private Random random;

    public PmxOperator(Long seed, double baseAttraction, double lightAbsorption) {
        this.seed = seed;
        this.lightAbsorption = lightAbsorption;
        this.baseAttraction = baseAttraction;

        if (seed != null)
            random = new Random(seed);
        else
            random = new Random();
    }

    @Override
    public Firefly[] apply(Firefly firefly1, Firefly firefly2) {
        Job[] parent1 = firefly1.getJobsDistribution();
        Job[] parent2 = firefly2.getJobsDistribution();

        int permutationLength = parent1.length;
        if (permutationLength != parent2.length)
            throw new IllegalArgumentException(
                String.format("Size of the first parent (%d) is not equal to the size of the second parent (%d)", parent1.length, parent2.length));

        Job[] firstOffspring = new Job[permutationLength];
        Job[] secondOffspring = new Job[permutationLength];

        int swathSize = random.nextInt(permutationLength - 2) + 2;
        int cuttingPoint1 = random.nextInt(permutationLength + 1 - swathSize);
        int cuttingPoint2 = cuttingPoint1 + swathSize - 1;

        // initialize replacement subchains
        Job[] replacement1 = new Job[permutationLength];
        Job[] replacement2 = new Job[permutationLength];

        for (int i = 0; i < permutationLength; i++)
            replacement1[i] = replacement2[i] = null;

        // copy the subchains
        for (int i = cuttingPoint1; i <= cuttingPoint2; i++) {
            firstOffspring[i] = parent2[i];
            secondOffspring[i] = parent1[i];

            replacement1[parent2[i].getId()] = parent1[i];
            replacement2[parent1[i].getId()] = parent2[i];
        }


        for (int i = 0; i < permutationLength; i++) {
            if ((i >= cuttingPoint1) && (i <= cuttingPoint2))
                continue ;

            Job n1 = parent1[i];
            Job m1 = replacement1[n1.getId()];

            Job n2 = parent2[i];
            Job m2 = replacement2[n2.getId()];

            while (m1 != null) {
                n1 = m1;
                m1 = replacement1[m1.getId()];
            }

            while (m2 != null) {
                n2 = m2;
                m2 = replacement2[m2.getId()];
            }

            firstOffspring[i] = n1;
            secondOffspring[i] = n2;
        }

        Firefly[] result = new Firefly[2];
        result[1] = new Firefly(firstOffspring, baseAttraction, lightAbsorption);
        result[0] = new Firefly(secondOffspring, baseAttraction, lightAbsorption);

        return result;
    }
}
