package pl.agh.bo.flowshop.solver;

import pl.agh.bo.flowshop.movement.MovementStrategyType;

public class SolverParameters {
    public int maxIterations, populationSize;
    public float absorptionCoefficient, baseAttraction;
    public MovementStrategyType movementStrategy;
    public boolean useCds, useNeh;

    public SolverParameters() {
        this.maxIterations = 200;
        this.populationSize = 20;

        this.absorptionCoefficient = 0.1f;
        this.baseAttraction = 1;

        this.movementStrategy = MovementStrategyType.PMX;

        this.useCds = false;
        this.useNeh = false;
    }

    public float betaProbability(int distance) {
        return 1.0f / (1.0f + absorptionCoefficient * distance * distance);
    }
}
