package pl.agh.bo.flowshop.algorithm;

import java.lang.Integer;
import java.util.HashMap;
import java.util.List;

import pl.agh.bo.flowshop.Evaluator.IEvaluator;
import pl.agh.bo.flowshop.Evaluator.MakespanEvaluator;
import pl.agh.bo.flowshop.solver.FreflyFactory;
import pl.agh.bo.flowshop.Job;

public class FireflyGenNEH implements IFireflyGen {

    private long fireflyQtt;
    private Job[] jobs;
    private FireflyFactory fireflyFactory;
    private IEvaluator evaluator;

    public FireflyGenNEH() {
        this.fireflyQtt = 20;
        this.fireflyFactory = new FireflyFactory(jobs);
        this.evaluator = new MakespanEvaluator();
    }

    public FireflyGenNEH(Job[] jobs) {
        this.jobs = jobs;
        this.fireflyQtt = 20;
        this.fireflyFactory = new FireflyFactory(jobs);
        this.evaluator = new MakespanEvaluator();
    }

    public void setJobs(Job[] jobs) {
        this.jobs = jobs;
    }

    public Map<Long, Firefly> generateFireflies(long initialSeed) {

        Operations.quickSort(jobs, 0, jobs.length);

        jobs = performMagic(jobs);

        Map<Long, Firefly> fireflyMap = new HashMap<Long, Firefly>();

        fireflyMap.put(0, new Firefly(jobs));

        for (long i = 1; i < fireflyQtt; i++) {
            fireflyMap.put(i, fireflyFactory.spawnRandom());
        }

        return fireflyMap;
    }

    private Job[] putNew(Job[] jobs, Job job) {

        Job[] newJobs = new Job[jobs.length+1];

        for (int i = 0; i < jobs.length; i++) {
            newJobs[i] = jobs[i];
        }

        newJobs[jobs.length] = job;

        return newJobs;
    }

    private void minimize(Job[] jobs) {

        evaluator.setJobs(jobs);

        long min = evaluator.evaluate();
        int pos = jobs.length - 1;

        for (int i = pos; i > 0; i--) {
            Operations.swap(jobs, i - 1, i);
            if (min > evaluator.evaluate()) {
                min = evaluator.evaluate();
                pos = i - 1;
            }
        }

        for (int i = 0; i < pos; i++) {
            Operations.swap(jobs, i, i + 1);
        }
    }

    private Job[] performMagic(Job[] jobs) {

        Job[] tmp = new Job[2];

        tmp.[0] = jobs.[0];
        tmp.[1] = jobs.[1];

        for (int i = 2; i < jobs.length; i++) {
            tmp = putNew(tmp, jobs[i]);
            minimize(tmp);
        }

        return tmp;
    }
}