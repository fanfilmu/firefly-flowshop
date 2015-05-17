package pl.agh.bo.flowshop.generator;

import pl.agh.bo.flowshop.Firefly;
import pl.agh.bo.flowshop.Job;

public interface Constructor {
    public Firefly apply(Job[] jobs);
}
