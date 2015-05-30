package pl.agh.bo.flowshop.solver;

import pl.agh.bo.flowshop.movement.MovementStrategy;
import pl.agh.bo.flowshop.movement.PmxStrategy;
import pl.agh.bo.flowshop.movement.SwapMovementStrategy;
import pl.agh.bo.flowshop.problem.FlowshopProblem;
import pl.agh.bo.flowshop.solution.FlowshopSolution;
import pl.agh.bo.flowshop.solution.FlowshopSolutionType;
import pl.agh.bo.flowshop.solution.SolutionFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import pl.agh.bo.flowshop.GUI.ResultsChart;

import java.util.*;

public class Solver {

    private final FlowshopProblem problem;
    private final SolverParameters parameters;

    private Map<FlowshopSolution, Long> fireflies;
    private SolutionFactory factory;
    private ResultsChart resultsChart;

    public Solver(FlowshopProblem problem, SolverParameters parameters) {
        this.problem = problem;
        this.parameters = parameters;

        this.fireflies = new HashMap<>();
    }

    public FlowshopSolution run(ResultsChart resultsChart) {
        FlowshopSolution bestSolution = generateInitialPopulation();
        this.resultsChart = resultsChart;

        // Start measuring time of algorithm
        long startTime = System.nanoTime();

        System.out.format("Generating fireflies...%n");

        MovementStrategy strategy;
        FlowshopSolution newSolution;
        long result, currentBest = fireflies.get(bestSolution);

        switch (parameters.movementStrategy) {
            case PMX: strategy = new PmxStrategy(); break;
            case SWAP: strategy = new SwapMovementStrategy(); break;
            default: throw new NotImplementedException();
        }

        for (int i = 0; i < parameters.maxIterations; i++) {
            for (FlowshopSolution reference : fireflies.keySet()) {
                for (FlowshopSolution current : fireflies.keySet()) {
                    if (reference.equals(current))
                        continue;
                    if (fireflies.get(current) <= fireflies.get(reference)) continue;

                    newSolution = strategy.move(current, reference, problem, parameters);
                    current.setOrderFrom(newSolution);
                    result = problem.evaluateSolution(current);

                    if (result < currentBest) {
                        bestSolution = current;
                        currentBest = result;
                    }
                    else if (result == currentBest){
                        current.setOrderFrom(factory.spawn());
                        result = problem.evaluateSolution(current);
                    }

                    fireflies.put(current, result);
                }
            }

            resultsChart.addResult(i, fireflies.get(bestSolution));
        }

        long stopTime = System.nanoTime();
        double elapsedTime = (stopTime - startTime)/(1000000000.0);
        resultsChart.showResults(bestSolution, elapsedTime);

        return bestSolution;
    }

    /**
     * Generates initial population, taking into account preferences about using
     * heuristics like CDS or NEH
     * @return Best solution generated
     */
    private FlowshopSolution generateInitialPopulation() {
        this.factory = new SolutionFactory(problem, FlowshopSolutionType.VECTOR, null);
        int totalRequired = parameters.populationSize;

        FlowshopSolution best = null, current = null;

        if (parameters.useCds) {
            factory.setGeneratorType(SolutionFactory.GeneratorType.CDS);
            best = generateFirefly(factory);
            totalRequired--;
        }

        if (parameters.useNeh) {
            factory.setGeneratorType(SolutionFactory.GeneratorType.NEH);
            current = generateFirefly(factory);
            if (best == null || fireflies.get(best) > fireflies.get(current)) best = current;

            totalRequired--;
        }

        factory.setGeneratorType(SolutionFactory.GeneratorType.RANDOM);
        while (totalRequired > 0) {
            current = generateFirefly(factory);
            if (best == null || fireflies.get(best) > fireflies.get(current)) best = current;
            totalRequired--;
        }

        return best;
    }

    private FlowshopSolution generateFirefly(SolutionFactory factory) {
        FlowshopSolution solution = factory.spawn();
        fireflies.put(solution, problem.evaluateSolution(solution));
        return solution;
    }
}
