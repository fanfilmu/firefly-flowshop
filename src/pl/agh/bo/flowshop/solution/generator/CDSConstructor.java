package pl.agh.bo.flowshop.solution.generator;

import pl.agh.bo.flowshop.problem.FlowshopProblem;
import pl.agh.bo.flowshop.solution.FlowshopSolution;
import pl.agh.bo.flowshop.solution.FlowshopSolutionType;
import pl.agh.bo.flowshop.solution.SolutionFactory;

import java.util.*;

/**
 * Implementacja algorytmu konstrukcyjnego CDS.
 * Źródła:
 * 1. https://nikhatshahin.wordpress.com/2011/10/18/ma0044-q5-explain-the-steps-involved-in-johnson%E2%80%99s-algorithm-and-cds-algorithm/
 * 2. http://faculty.ksu.edu.sa/ialharkan/IE428/Chapter_4.pdf
 */
public class CDSConstructor implements Constructor {
    public FlowshopSolution apply(FlowshopProblem problem, FlowshopSolutionType type) {
        FlowshopSolution bestSolution = null;
        long bestSolutionEvaluation = Integer.MAX_VALUE;

        SurrogateProblemIterator jobSetIterator = new SurrogateProblemIterator(problem);

        for (FlowshopProblem surrogateProblem : jobSetIterator) {
            List<Integer> sptSet = new LinkedList<>();
            List<Integer> lptSet = new LinkedList<>();
            divideSet(surrogateProblem, sptSet, lptSet);
            sortSets(surrogateProblem, sptSet, lptSet);

            FlowshopSolution solution = new SolutionFactory(problem, type, SolutionFactory.GeneratorType.EMPTY).spawn();
            joinSets(solution, sptSet, lptSet);

            long evaluationResult = problem.evaluateSolution(solution);
            if (bestSolutionEvaluation > evaluationResult) {
                bestSolutionEvaluation = evaluationResult;
                bestSolution = solution;
            }
        }

        return bestSolution;
    }

    private void joinSets(FlowshopSolution solution, List<Integer> sptSet, List<Integer> lptSet) {
        int i = 0;
        sptSet.addAll(lptSet);
        for (int id : sptSet) {
            solution.set(i, id);
            i++;
        }
    }

    private void sortSets(FlowshopProblem surrogateProblem, List<Integer> sptSet, List<Integer> lptSet) {
        sptSet.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return surrogateProblem.jobs[o1][0] - surrogateProblem.jobs[o2][0];
            }
        });

        lptSet.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return surrogateProblem.jobs[o2][1] - surrogateProblem.jobs[o1][1];
            }
        });
    }

    private void divideSet(FlowshopProblem surrogateProblem, List<Integer> sptSet, List<Integer> lptSet) {
        for (int i = 0; i < surrogateProblem.jobCount; i++) {
            if (surrogateProblem.jobs[i][0] < surrogateProblem.jobs[i][1])
                sptSet.add(i);
            else
                lptSet.add(i);
        }
    }

    private class SurrogateProblemIterator implements Iterator<FlowshopProblem>, Iterable<FlowshopProblem> {
        private int currentProblem;
        private FlowshopProblem problem;

        public SurrogateProblemIterator(FlowshopProblem problem) {
            this.problem = problem;
            currentProblem = 1;
        }

        @Override
        public boolean hasNext() {
            return currentProblem < problem.operationCount;
        }

        @Override
        public FlowshopProblem next() {
            FlowshopProblem surrogateProblem = new FlowshopProblem();
            surrogateProblem.jobCount = problem.jobCount;
            surrogateProblem.operationCount = 2;
            surrogateProblem.jobs = new int[problem.jobCount][2];

            int firstOpTime;
            int secondOpTime;

            for (int i = 0; i < problem.jobCount; i++) {
                firstOpTime = 0;
                secondOpTime = 0;
                int[] currentJobOperationTimes = problem.jobs[i];

                for (int j = 0; j < currentProblem; j++) {
                    firstOpTime += currentJobOperationTimes[j];
                    secondOpTime += currentJobOperationTimes[problem.operationCount - 1 - j];
                }

                surrogateProblem.jobs[i][0] = firstOpTime;
                surrogateProblem.jobs[i][1] = secondOpTime;
            }

            currentProblem++;
            return surrogateProblem;
        }

        @Override
        public Iterator<FlowshopProblem> iterator() {
            return this;
        }
    }
}
