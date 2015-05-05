package pl.agh.bo.flowshop.crossover;

import com.sun.org.apache.bcel.internal.generic.SWAP;
import pl.agh.bo.flowshop.Firefly;
import pl.agh.bo.flowshop.Job;
import pl.agh.bo.flowshop.solver.HammingDistance;

import java.util.Random;

public class SwapOperator implements CrossoverOperator
{
    private static final int MIN_SWAP_NUM = 1;

    private static final int MAX_SWAP_NUM = 5;

    private Random randomGenerator = null;

    private double baseAttraction;

    private double lightAbsorption;

    public SwapOperator(double baseAttraction, double lightAbsorption) {
        this.randomGenerator = new Random();
        this.baseAttraction = baseAttraction;
        this.lightAbsorption = lightAbsorption;
    }

    public SwapOperator(long seed, double baseAttraction, double lightAbsorption)
    {
        this.randomGenerator = new Random(seed);
        this.baseAttraction = baseAttraction;
        this.lightAbsorption = lightAbsorption;
    }

    @Override
    public Firefly[] apply(Firefly fireflyA, Firefly fireflyB)
    {
        Job[] parentA = fireflyA.getJobsDistribution();
        Job[] parentB = fireflyB.getJobsDistribution();

        if (parentA.length != parentB.length) {
            throw new IllegalArgumentException("Not equal population sizes");
        }

        int numSwaps = getNumberOfSwaps(fireflyA, fireflyB);

        Job[] childA = swap(parentA, parentB, numSwaps);
        Job[] childB = swap(parentB, parentA, numSwaps);

        Firefly fireflyChildA = new Firefly(childA, baseAttraction, lightAbsorption);
        Firefly fireflyChildB = new Firefly(childB, baseAttraction, lightAbsorption);

        return new Firefly[] { fireflyChildA, fireflyChildB };
    }

    private Job[] swap(Job[] reference, Job[] target, int numSwaps)
    {
        Job[] result = target.clone();

        int populationSize = reference.length;

        int i = 0, j = 0;

        while (i < populationSize && numSwaps > 0) {
            if (result[i] != reference[i]) {
                j = i + 1;

                while (j < populationSize && result[j] != reference[i]) j++;

                result[j] = result[i];
                result[i] = reference[i];

                numSwaps--;
            }

            i++;
        }

        return result;
    }

    private int getNumberOfSwaps(Firefly fireflyA, Firefly fireflyB)
    {
        float swapFraction = randomGenerator.nextFloat();

        int distance = HammingDistance.calculateDistance(fireflyA, fireflyB);

        return Math.round(swapFraction * distance);
    }
}
