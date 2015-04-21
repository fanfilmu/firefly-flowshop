package pl.agh.bo.flowshop.Evaluator;

import pl.agh.bo.flowshop.Job;

import java.util.List;

/**
 * Created by Andrzej on 2015-04-21.
 */
public interface IEvaluator {
    public void setJobs(List<Job> jobs);
    public long evaluate();
}
