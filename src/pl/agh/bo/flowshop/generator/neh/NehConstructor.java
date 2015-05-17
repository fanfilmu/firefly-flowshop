package pl.agh.bo.flowshop.generator.neh;

import pl.agh.bo.flowshop.evaluator.IEvaluator;
import pl.agh.bo.flowshop.evaluator.MakespanEvaluator;
import pl.agh.bo.flowshop.Firefly;
import pl.agh.bo.flowshop.Job;
import pl.agh.bo.flowshop.generator.Constructor;

import java.util.Arrays;

public class NehConstructor implements Constructor {
    private double baseAttraction;

    private double lightAbsorption;

    public NehConstructor(double baseAttraction, double lightAbsorption) {
        this.baseAttraction = baseAttraction;
        this.lightAbsorption = lightAbsorption;
    }

    @Override
    public Firefly apply(Job[] jobs) {
        Operations.quickSort(jobs, 0, jobs.length - 1);

        jobs = performMagic(jobs);

        return  new Firefly(jobs, baseAttraction, lightAbsorption);
    }

    private Job[] performMagic(Job[] jobs) {

        Job[] tmp = new Job[2];

        tmp[0] = jobs[0];
        tmp[1] = jobs[1];

        for (int i = 2; i < jobs.length; i++) {
            tmp = putNew(tmp, jobs[i]);
            minimize(tmp);
        }

        return tmp;
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
        IEvaluator evaluator = new MakespanEvaluator();

        evaluator.setJobs(Arrays.asList(jobs));

        long min = evaluator.evaluate();
        int pos = jobs.length - 1;

        for (int i = pos; i > 0; i--) {

            Operations.swap(jobs, i - 1, i);

            evaluator.setJobs(Arrays.asList(jobs));

            if (min > evaluator.evaluate()) {
                min = evaluator.evaluate();
                pos = i - 1;
            }
        }

        for (int i = 0; i < pos; i++) {
            Operations.swap(jobs, i, i + 1);
        }
    }
}