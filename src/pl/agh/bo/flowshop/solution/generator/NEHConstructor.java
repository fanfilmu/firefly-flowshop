package pl.agh.bo.flowshop.solution.generator;

import pl.agh.bo.flowshop.problem.FlowshopProblem;
import pl.agh.bo.flowshop.solution.FlowshopSolution;
import pl.agh.bo.flowshop.solution.FlowshopSolutionType;
import pl.agh.bo.flowshop.solution.SolutionFactory;

import java.util.LinkedList;
import java.util.stream.IntStream;

public class NEHConstructor implements Constructor {

    private int stepAmount;

    /**
     * NEH heuristic constructor. Cap amount of steps with stepAmount parameter
     * @param stepAmount maximum amount of steps (-1 for full algorithm)
     */
    public NEHConstructor(int stepAmount) {
        this.stepAmount = stepAmount;
    }

    @Override
    public FlowshopSolution apply(FlowshopProblem problem, FlowshopSolutionType type) {
        // clone jobs
        LinkedList<JobStruct> jobs = new LinkedList<>();
        for (int i = 0; i < problem.jobCount; i++)
            jobs.add(new JobStruct(i, problem.jobs[i].clone()));

        // sort jobs
        jobs.sort((o1, o2) -> IntStream.of(o2.getOperationTimes()).sum() - IntStream.of(o1.getOperationTimes()).sum());

        FlowshopSolution globalSolution = new SolutionFactory(problem, type, SolutionFactory.GeneratorType.EMPTY).spawn();

        int mapping[] = new int[problem.jobCount]; //mapping between tempProblem jobs and global problem
        for (int i = 0; i < problem.jobCount; i++) {
            JobStruct js = jobs.get(i);
            mapping[i] = js.getId(); // problem.get(mapping[i]) <=> tempProblem.get(i)
        }

        // create initial problem
        FlowshopProblem tempProblem = generateInitialProblem(problem, jobs);

        FlowshopSolution bestSolution = new SolutionFactory(problem, type, SolutionFactory.GeneratorType.EMPTY).spawn();
        bestSolution.set(0, 0);
        FlowshopSolution tempSolution = bestSolution.clone();
        long bestResult, tempResult;
        setStepAmount(problem);

        for (int i = 1; i < stepAmount; i++) {
            tempProblem.jobCount++;
            tempProblem.jobs[i] = jobs.get(i).getOperationTimes();

            tempSolution.set(i, i);
            bestSolution.set(i, i);

            bestResult = tempProblem.evaluateSolution(bestSolution);

            for (int j = 0; j < i; j++) {
                tempSolution.swap(i - j, i - j - 1);
                if (bestResult > (tempResult = tempProblem.evaluateSolution(tempSolution))) {
                    bestSolution = tempSolution.clone();
                    bestResult = tempResult;
                }
            }
        }

        for (int i = 0; i < stepAmount; i++) {
            globalSolution.set(i, mapping[bestSolution.get(i)]);
        }

        for (int i = stepAmount; i < problem.jobCount; i++) {
            globalSolution.set(i, mapping[i]);
        }

        return globalSolution;
    }

    private void setStepAmount(FlowshopProblem problem) {
        if (stepAmount == -1) stepAmount = problem.jobCount;
        else stepAmount = Math.max(1, Math.min(problem.jobCount, stepAmount));
    }

    private FlowshopProblem generateInitialProblem(FlowshopProblem problem, LinkedList<JobStruct> jobs) {
        FlowshopProblem tempProblem = new FlowshopProblem();
        tempProblem.jobCount = 1;
        tempProblem.operationCount = problem.operationCount;
        tempProblem.jobs = new int[problem.jobCount][];
        tempProblem.jobs[0] = jobs.get(0).getOperationTimes();

        return tempProblem;
    }

    private class JobStruct {
        private int id;
        private int[] operationTimes;

        public JobStruct(int id, int[] operationTimes) {
            this.id = id;
            this.operationTimes = operationTimes;
        }

        public int getId() {
            return id;
        }

        public int[] getOperationTimes() {
            return operationTimes;
        }
    }
}