package pl.agh.bo.flowshop.solution;

/**
 * Created by fan on 18.05.15.
 */
public class InvalidJobOrderException extends Exception {
    public InvalidJobOrderException() {
        super("Requested job order is invalid");
    }
}
