package pl.agh.bo.flowshop;

import pl.agh.bo.flowshop.solver.HammingDistance;

import java.util.Arrays;

/**
 * Created by Bartosz Zurkowski on 20.04.15.
 */
public class Firefly {

    private static final double baseAttraction = 1.0;

    private static final double lightAbsorption = 0.05;

    private Job[] jobsDistribution = null;

    private long lightIntensity;

    public Firefly(Job[] jobsDistribution) {
        this.jobsDistribution = jobsDistribution;
    }

    public double getAttractiveness(Firefly other) {
        int distance = HammingDistance.calculateDistance(this, other);

        return baseAttraction * Math.exp((double) -lightAbsorption * distance);
    }

    public Job[] getJobsDistribution() {
        return jobsDistribution;
    }

    public void setJobsDistribution(Job[] jobsDistribution) {
        this.jobsDistribution = jobsDistribution;
    }

    public long getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(long lightIntensity) {
        this.lightIntensity = lightIntensity;
    }

    @Override
    public String toString() {
        return "Firefly{" +
                "lightIntensity=" + lightIntensity +
                ", jobsDistribution=" + Arrays.toString(jobsDistribution) +
                '}';
    }
}
