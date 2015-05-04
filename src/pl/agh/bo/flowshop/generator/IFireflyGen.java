package pl.agh.bo.flowshop.generator;

import pl.agh.bo.flowshop.Firefly;
import pl.agh.bo.flowshop.Job;

import java.util.List;
import java.util.Map;

public interface IFireflyGen {

    Map<Long, Firefly> generateFireflies(long initialSeed);
    void setJobs(Job[] jobs);
    void setJobs(List<Job> jobs);
}