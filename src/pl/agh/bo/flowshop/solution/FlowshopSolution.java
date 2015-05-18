package pl.agh.bo.flowshop.solution;

import java.util.Iterator;

/**
 * This interface represents specific solution of the flowshop problem,
 * that is an order of jobs to be executed. It must implement Iterator<Integer>
 * interface, allowing users to iterate job IDs.
 */
public interface FlowshopSolution {
    /**
     * Returns ID of the job at specified index
     * @param index Position of the job in the solution
     * @return ID of the job at specified index
     */
    int get(int index);

    /**
     * Places job jobId in index place
     * @param index Position at which job should be placed
     * @param jobId Job id to place
     * @throws InvalidJobOrderException
     */
    void set(int index, int jobId);

    /**
     * Swaps two job position
     * @param position1 First job to swap
     * @param position2 Second job to swap
     */
    void swap(int position1, int position2);

    /**
     * Get order of the jobs (by id)
     * @return Array of job IDs
     */
    int[] getOrder();

    /**
     * Get length of the solution
     * @return Length of the solution
     */
    int getLength();
}
