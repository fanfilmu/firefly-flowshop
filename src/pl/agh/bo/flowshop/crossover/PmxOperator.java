package pl.agh.bo.flowshop.crossover;

import pl.agh.bo.flowshop.Firefly;

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
    private double absorptionCoefficient;

    public PmxOperator() {
        absorptionCoefficient = 0.4;
    }

    public PmxOperator(Long seed, double absorptionCoefficient) {
        this.seed = seed;
        this.absorptionCoefficient = absorptionCoefficient;
    }

    @Override
    public Firefly[] apply(Firefly firefly1, Firefly firefly2) {
        int[] parent1 = firefly1.getJobsDistribution();
        int[] parent2 = firefly2.getJobsDistribution();

        int permutationLength = parent1.length;
        if (permutationLength != parent2.length)
            throw new IllegalArgumentException(
                String.format("Size of the first parent (%d) is not equal to the size of the second parent (%d)", parent1.length, parent2.length));

        Random random;
        if (seed != null)
            random = new Random(seed);
        else
            random = new Random();
        int[] firstOffspring = new int[permutationLength];
        int[] secondOffspring = new int[permutationLength];

        int cuttingPoint1;
        int cuttingPoint2;

        int maxSwathSize = (int) Math.ceil(1.2 * absorptionCoefficient * permutationLength);
        int minSwathSize = (int) Math.ceil(0.6 * absorptionCoefficient * permutationLength);


        // Choosing cutting points
        cuttingPoint1 = random.nextInt(permutationLength);
        cuttingPoint2 = random.nextInt(permutationLength);
        while (cuttingPoint2 == cuttingPoint1)
            cuttingPoint2 = random.nextInt(permutationLength);

        if (cuttingPoint1 > cuttingPoint2) {
            int swap = cuttingPoint1;
            cuttingPoint1 = cuttingPoint2;
            cuttingPoint2 = swap;
        }

        if (cuttingPoint2 - cuttingPoint1 > maxSwathSize)
            cuttingPoint2 = cuttingPoint1 + maxSwathSize;
        if (cuttingPoint2 - cuttingPoint1 < minSwathSize) {
            if (cuttingPoint1 + minSwathSize >= permutationLength) {
                cuttingPoint1 = permutationLength - minSwathSize;
                cuttingPoint2 = permutationLength - 1;
            }
            else
                cuttingPoint2 = cuttingPoint1 + minSwathSize;
        }

        System.out.format("%d - %d (%d - %d)%n", cuttingPoint1, cuttingPoint2, minSwathSize, maxSwathSize);

        // initialize replacement subchains
        Integer[] replacement1 = new Integer[permutationLength];
        Integer[] replacement2 = new Integer[permutationLength];

        for (int i = 0; i < permutationLength; i++)
            replacement1[i] = replacement2[i] = -1;

        // copy the subchains
        for (int i = cuttingPoint1; i <= cuttingPoint2; i++) {
            firstOffspring[i] = parent2[i];
            secondOffspring[i] = parent1[i];

            replacement1[parent2[i]] = parent1[i];
            replacement2[parent1[i]] = parent2[i];
        }


        for (int i = 0; i < permutationLength; i++) {
            if ((i >= cuttingPoint1) && (i <= cuttingPoint2))
                continue ;

            int n1 = parent1[i];
            int m1 = replacement1[n1];

            int n2 = parent2[i];
            int m2 = replacement2[n2];

            while (m1 != -1) {
                n1 = m1;
                m1 = replacement1[m1];
            }

            while (m2 != -1) {
                n2 = m2;
                m2 = replacement2[m2];
            }

            firstOffspring[i] = n1;
            secondOffspring[i] = n2;
        }

        Firefly[] result = new Firefly[2];
        result[1] = new Firefly(firstOffspring);
        result[0] = new Firefly(secondOffspring);

        return result;
    }

    public double getAbsorptionCoefficient() {
        return absorptionCoefficient;
    }

    public void setAbsorptionCoefficient(double absorptionCoefficient) {
        this.absorptionCoefficient = absorptionCoefficient;
    }
}
