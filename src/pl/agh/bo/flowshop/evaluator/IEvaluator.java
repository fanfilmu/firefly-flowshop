package pl.agh.bo.flowshop.evaluator;

/**
 * Created by Andrzej on 2015-04-21.
 */
public interface IEvaluator {
    public void setJobs(int[][] jobs);
    public long evaluate();
}
