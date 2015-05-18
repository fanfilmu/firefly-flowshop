package pl.agh.bo.flowshop.movement;

import pl.agh.bo.flowshop.solution.FlowshopSolution;
import pl.agh.bo.flowshop.solution.VectorFlowshopSolution;
import pl.agh.bo.flowshop.solver.HammingDistance;

import java.util.Random;

/**
 * Created by fan on 18.05.15.
 */
public abstract class AbstractMovementStrategy implements MovementStrategy {
    protected Random random;

    public AbstractMovementStrategy() {
        random = new Random();
    }

    protected int calculateDistance(FlowshopSolution solution1, FlowshopSolution solution2) {
        if (solution1 instanceof VectorFlowshopSolution && solution2 instanceof VectorFlowshopSolution)
            return HammingDistance.calculateDistance(solution1, solution2);
        else return -1; //NEXT: other types of calculating distance for different representations
    }
}
