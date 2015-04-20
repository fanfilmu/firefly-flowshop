package pl.agh.bo.flowshop;

import pl.agh.bo.flowshop.algorithm.HammingDistance;

/**
 * Created by Bartosz Zurkowski on 20.04.15.
 */
public class Firefly {

    private static final double baseAttraction = 1.0;

    private static final double lightAbsorption = 0.05;

    private int[] jobsDistribution;

    private double lightIntensity;

    public Firefly(int[] jobsDistribution) {
        this.jobsDistribution = jobsDistribution;
    }

    public double getAttractiveness(Firefly other) {
        int distance = HammingDistance.calculateDistance(this, other);

        return baseAttraction * Math.exp((double) -lightAbsorption * distance);
    }

    public int[] getJobsDistribution() {
        return jobsDistribution;
    }

    public void setJobsDistribution(int[] jobsDistribution) {
        this.jobsDistribution = jobsDistribution;
    }

    public double getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(double lightIntensity) {
        this.lightIntensity = lightIntensity;
    }
}
