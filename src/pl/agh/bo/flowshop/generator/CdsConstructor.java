package pl.agh.bo.flowshop.generator;

import pl.agh.bo.flowshop.evaluator.IEvaluator;
import pl.agh.bo.flowshop.evaluator.MakespanEvaluator;
import pl.agh.bo.flowshop.Firefly;
import pl.agh.bo.flowshop.Job;

import java.util.*;

/**
 * Implementacja algorytmu konstrukcyjnego CDS.
 * Źródła:
 * 1. https://nikhatshahin.wordpress.com/2011/10/18/ma0044-q5-explain-the-steps-involved-in-johnson%E2%80%99s-algorithm-and-cds-algorithm/
 * 2. http://faculty.ksu.edu.sa/ialharkan/IE428/Chapter_4.pdf
 */
public class CdsConstructor implements Constructor {
    private double baseAttraction;

    private double lightAbsorption;

    public CdsConstructor(double baseAttraction, double lightAbsorption) {
        this.baseAttraction = baseAttraction;
        this.lightAbsorption = lightAbsorption;
    }

    public Firefly apply(Job[] jobs) {
        int jobCount = jobs.length;
        Job[] bestSolution = null;
        long bestSolutionEvaluation = Integer.MAX_VALUE;

        SurrogateProblemIterator jobSetIterator = new SurrogateProblemIterator(jobs);
        for (List<Job> surrogateProblem : jobSetIterator) {
            Job[] result = new Job[jobCount];

            List<Job> sptSet = new LinkedList<Job>();
            List<Job> lptSet = new LinkedList<Job>();
            divideSet(surrogateProblem, sptSet, lptSet);
            sortSets(sptSet, lptSet);
            joinSets(jobs, result, sptSet, lptSet);

            IEvaluator evaluator = new MakespanEvaluator();
            evaluator.setJobs(Arrays.asList(result));

            long evaluationResult = evaluator.evaluate();
            if (bestSolutionEvaluation > evaluationResult) {
                bestSolutionEvaluation = evaluationResult;
                bestSolution = result;
            }
        }

        return new Firefly(bestSolution, baseAttraction, lightAbsorption);
    }

    private void joinSets(Job[] problem, Job[] result, List<Job> sptSet, List<Job> lptSet) {
        int i = 0;
        for (Job tempJob : sptSet) {
            for (Job job : problem) {
                if (job.getId() == tempJob.getId()) {
                    result[i] = job;
                    i++;
                    break;
                }
            }
        }

        for (Job tempJob : lptSet) {
            for (Job job : problem) {
                if (job.getId() == tempJob.getId()) {
                    result[i] = job;
                    i++;
                    break;
                }
            }
        }
    }

    private void sortSets(List<Job> sptSet, List<Job> lptSet) {
        sptSet.sort(new Comparator<Job>() {
            @Override
            public int compare(Job o1, Job o2) {
                return o1.getOperationTimes()[0] - o2.getOperationTimes()[0];
            }
        });

        lptSet.sort(new Comparator<Job>() {
            @Override
            public int compare(Job o1, Job o2) {
                return o2.getOperationTimes()[1] - o1.getOperationTimes()[1];
            }
        });
    }

    private void divideSet(List<Job> surrogateProblem, List<Job> sptSet, List<Job> lptSet) {
        Integer[] operationTimes = null;

        for (Job job : surrogateProblem) {
            operationTimes = job.getOperationTimes();
            if (operationTimes[0] < operationTimes[1])
                sptSet.add(job);
            else
                lptSet.add(job);
        }
    }

    private class SurrogateProblemIterator implements Iterator<List<Job>>, Iterable<List<Job>> {
        private int machineCount;
        private int jobsCount;
        private int currentProblem;
        private Job[] originalJobs;

        public SurrogateProblemIterator(Job[] jobs) {
            originalJobs = jobs;
            jobsCount = jobs.length;
            machineCount = jobs[0].getOperationTimes().length;
            currentProblem = 1;
        }

        @Override
        public boolean hasNext() {
            return currentProblem < machineCount;
        }

        @Override
        public List<Job> next() {
            Job[] result = new Job[jobsCount];

            int firstOpTime;
            int secondOpTime;

            for (int i = 0; i < jobsCount; i++) {
                firstOpTime = 0;
                secondOpTime = 0;
                Integer[] currentJobOperationTimes = originalJobs[i].getOperationTimes();

                for (int j = 0; j < currentProblem; j++) {
                    firstOpTime += currentJobOperationTimes[j];
                    secondOpTime += currentJobOperationTimes[machineCount - 1 - j];
                }

                result[i] = new Job(originalJobs[i].getId(), new Integer[] { firstOpTime, secondOpTime });
            }

            currentProblem++;
            return  new LinkedList<Job>(Arrays.asList(result));
        }

        @Override
        public Iterator<List<Job>> iterator() {
            return this;
        }
    }
}
