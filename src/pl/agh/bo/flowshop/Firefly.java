    package pl.agh.bo.flowshop;

/**
 * Created by Bartosz Zurkowski on 20.04.15.
 */
public class Firefly {

    private int[] jobsDistribution;

    private double baseAttraction;

    private double lightAbsorption;

    private double lightIntensity;

    public Firefly(int[] jobsDistribution, double baseAttraction, double lightAbsorption) {
        this.jobsDistribution = jobsDistribution;
        this.baseAttraction   = baseAttraction;

        this.lightAbsorption  = lightAbsorption;
    }

    public double getAttractiveness(Firefly other) {
        // int distance = HammingMeasure.calculateDistance(this, other);

        int distance = 2;

        return baseAttraction * Math.exp((double) -lightAbsorption * distance);
    }

    public int[] getJobsDistribution() {
        return jobsDistribution;
    }

    public void setJobsDistribution(int[] jobsDistribution) {
        this.jobsDistribution = jobsDistribution;
    }

    public double getBaseAttraction() {
        return baseAttraction;
    }

    public void setBaseAttraction(double baseAttraction) {
        this.baseAttraction = baseAttraction;
    }

    public double getLightAbsorption() {
        return lightAbsorption;
    }

    public void setLightAbsorption(double lightAbsorption) {
        this.lightAbsorption = lightAbsorption;
    }

    public double getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(double lightIntensity) {
        this.lightIntensity = lightIntensity;
    }
}
