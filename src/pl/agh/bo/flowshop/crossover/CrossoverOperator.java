package pl.agh.bo.flowshop.crossover;

import pl.agh.bo.flowshop.Firefly;

public interface CrossoverOperator {
    Firefly[] apply(Firefly parent1, Firefly parent2);
}
