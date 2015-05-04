package pl.agh.bo.flowshop;

import pl.agh.bo.flowshop.solver.HammingDistance;

import java.util.Arrays;

/**
 * Created by Bartosz Zurkowski on 20.04.15.
 */
public class Firefly {

    private double mBaseAttraction;
    private double mLightAbsorption;
    private Job[] mJobsDistribution = null;

    private long lightIntensity;

    public Firefly(Job[] jobsDistribution, double baseAttraction, double lightAbsorption) {
        mJobsDistribution = jobsDistribution;
        mBaseAttraction = baseAttraction;
        mLightAbsorption = lightAbsorption;
    }

    public double getAttractiveness(Firefly other) {
        int distance = HammingDistance.calculateDistance(this, other);

        return mBaseAttraction * Math.exp((double) -mLightAbsorption * distance);
    }

    public Job[] getJobsDistribution() {
        return mJobsDistribution;
    }

    public void setJobsDistribution(Job[] jobsDistribution) {
        mJobsDistribution = jobsDistribution;
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
                ", jobsDistribution=" + Arrays.toString(mJobsDistribution) +
                '}';
    }
}
