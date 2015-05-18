package pl.agh.bo.flowshop.solution;

import pl.agh.bo.flowshop.problem.FlowshopProblem;
import pl.agh.bo.flowshop.solution.generator.CDSConstructor;
import pl.agh.bo.flowshop.solution.generator.NEHConstructor;

import java.util.Random;

public class SolutionFactory {
    private FlowshopSolutionType solutionType;
    private GeneratorType generatorType;
    private FlowshopProblem problem;

    public enum GeneratorType { RANDOM, CDS, NEH, EMPTY }

    public SolutionFactory(FlowshopProblem problem, FlowshopSolutionType solutionType, GeneratorType generatorType) {
        this.problem = problem;
        this.solutionType = solutionType;
        this.generatorType = generatorType;
    }

    public void setSolutionType(FlowshopSolutionType solutionType) {
        this.solutionType = solutionType;
    }

    public void setGeneratorType(GeneratorType generatorType) {
        this.generatorType = generatorType;
    }

    public FlowshopSolution spawn() {
        FlowshopSolution result = createSolutionObject();
        GenerationStrategy strategy = getGenerationStrategy();

        strategy.populate(problem, result);
        return result;
    }

    private GenerationStrategy getGenerationStrategy() {
        switch (generatorType) {
            case RANDOM: return new RandomGenerationStrategy();
            case CDS: return new CdsGenerationStartegy();
            case NEH: return new NehGenerationStrategy();
            case EMPTY: return new EmptyGenerationStrategy();
            default: throw new IllegalArgumentException();
        }
    }

    private FlowshopSolution createSolutionObject() {
        switch (solutionType) {
            case VECTOR: return new VectorFlowshopSolution(problem.jobCount);
            default: throw new IllegalArgumentException();
        }
    }

    private interface GenerationStrategy {
        void populate(FlowshopProblem problem, FlowshopSolution solution);
    }

    private class RandomGenerationStrategy implements GenerationStrategy {
        @Override
        public void populate(FlowshopProblem problem, FlowshopSolution solution) {
            Random random = new Random();
            int proposedPlace;

            for (int i = 0; i < problem.jobCount; i++) {
                // find first available place for the job id: i
                proposedPlace = random.nextInt(problem.jobCount);
                while (solution.get(proposedPlace) != -1)
                    proposedPlace = (proposedPlace + 1) % problem.jobCount;

                solution.set(proposedPlace, i);
            }
        }
    }

    private class NehGenerationStrategy implements GenerationStrategy {
        @Override
        public void populate(FlowshopProblem problem, FlowshopSolution solution) {
            NEHConstructor constructor = new NEHConstructor(-1);
            FlowshopSolution result = constructor.apply(problem, solution.getType());
            for (int i = 0; i < problem.jobCount; i++)
                solution.set(i, result.get(i));
        }
    }

    private class EmptyGenerationStrategy implements GenerationStrategy {
        @Override
        public void populate(FlowshopProblem problem, FlowshopSolution solution) {
            for (int i = 0; i < problem.jobCount; i++)
                solution.set(i, -1);
        }
    }

    private class CdsGenerationStartegy implements GenerationStrategy {
        @Override
        public void populate(FlowshopProblem problem, FlowshopSolution solution) {
            CDSConstructor constructor = new CDSConstructor();
            FlowshopSolution result = constructor.apply(problem, solution.getType());
            for (int i = 0; i < problem.jobCount; i++)
                solution.set(i, result.get(i));
        }
    }
}
