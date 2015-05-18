package pl.agh.bo.flowshop.problem;

public class FlowshopProblemBuilder {
    int jobCount, operationCount, lowerBound, upperBound;
    long initialSeed;

    public FlowshopProblemBuilder setJobCount(int jobCount) {
        this.jobCount = jobCount;
        return this;
    }

    public FlowshopProblemBuilder setJobCount(String jobCount) {
        this.jobCount = Integer.parseInt(jobCount);
        return this;
    }

    public FlowshopProblemBuilder setOperationCount(int operationCount) {
        this.operationCount = operationCount;
        return this;
    }

    public FlowshopProblemBuilder setOperationCount(String operationCount) {
        this.operationCount = Integer.parseInt(operationCount);
        return this;
    }

    public FlowshopProblemBuilder setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
        return this;
    }

    public FlowshopProblemBuilder setLowerBound(String lowerBound) {
        this.lowerBound = Integer.parseInt(lowerBound);
        return this;
    }

    public FlowshopProblemBuilder setUpperBound(int upperBound) {
        this.upperBound = upperBound;
        return this;
    }

    public FlowshopProblemBuilder setUpperBound(String upperBound) {
        this.upperBound = Integer.parseInt(upperBound);
        return this;
    }

    public FlowshopProblemBuilder setInitialSeed(long initialSeed) {
        this.initialSeed = initialSeed;
        return this;
    }

    public FlowshopProblemBuilder setInitialSeed(String initialSeed) {
        this.initialSeed = Integer.parseInt(initialSeed);
        return this;
    }

    public FlowshopProblem build() {
        FlowshopProblem result = new FlowshopProblem();

        result.jobCount = jobCount;
        result.operationCount = operationCount;
        result.initialSeed = initialSeed;
        result.lowerBound = lowerBound;
        result.upperBound = upperBound;

        return result;
    }
}
