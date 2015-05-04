package pl.agh.bo.flowshop.algorithm;

public interface IFireflyGen {

    Map<Long, Firefly> generateFireflies(long initialSeed);
    void setJobs(Job[] jobs);
}