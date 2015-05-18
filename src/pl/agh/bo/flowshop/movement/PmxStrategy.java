package pl.agh.bo.flowshop.movement;

import pl.agh.bo.flowshop.problem.FlowshopProblem;
import pl.agh.bo.flowshop.solution.FlowshopSolution;
import pl.agh.bo.flowshop.solver.SolverParameters;

import java.util.Arrays;
import java.util.Random;

import static java.lang.System.exit;

public class PmxStrategy extends AbstractMovementStrategy {
    // http://www.rubicite.com/Tutorials/GeneticAlgorithms/CrossoverOperators/PMXCrossoverOperator.aspx
    @Override
    public FlowshopSolution move(FlowshopSolution first, FlowshopSolution second, FlowshopProblem problem, SolverParameters params) {
        int currentDistance = calculateDistance(first, second);
        FlowshopSolution newSolution;

        try {
            newSolution = first.getNewInstance();
        } catch(Exception e) {
            e.printStackTrace();
            exit(1);
            return null;
        }

        int cuttingPoint = random.nextInt(problem.jobCount);
        int swathSize = calculateSwathSize(currentDistance, problem.jobCount, params);
        int temp;

        int mapping[] = new int[problem.jobCount];
        Arrays.fill(mapping, -1);

        // copy swath
        for (int i = cuttingPoint; swathSize > 0; swathSize--) {
            temp = second.get(i);
            mapping[temp] = i;
            newSolution.set(i, temp);
            i = (i + 1) % problem.jobCount;
        }

        int value;
        for (int i = 0; i < problem.jobCount; i++) {
            value = first.get(i);

            if (mapping[value] == -1) {
                temp = newSolution.get(i);

                if (temp == -1) {
                    mapping[value] = i;
                    newSolution.set(i, value);
                }
            }
        }

        temp = 0;
        for (int i = 0; i < problem.jobCount; i++) {
            if (mapping[i] == -1) {
                while (newSolution.get(temp) != -1) temp++;
                newSolution.set(temp, i);
            }
        }

        return newSolution;
    }

    private int calculateSwathSize(int currentDistance, int jobCount, SolverParameters params) {
        int size = jobCount - currentDistance; // new firefly should be closer to the better one
        size += random.nextInt((int)(currentDistance / (2 - params.absorptionCoefficient)));

        return size;
    }
}
