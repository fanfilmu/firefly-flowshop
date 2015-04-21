package pl.agh.bo.flowshop.Evaluator;

import pl.agh.bo.flowshop.Evaluator.IEvaluator;
import pl.agh.bo.flowshop.Job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * This class evaluates the amount of time needed to process all jobs in a
 * specific order. Only one operation per job at one time is permitted.
 *
 * Created by Andrzej on 2015-04-20.
 */
public class MakespanEvaluator implements IEvaluator {

    private List<Job> jobs;
    private int jobsAmount;
    private Integer[][] operationTimes;
    // Keep this so as not to waste time looking at rows with all zeros
    private int firstNonZeroRow;

    /**
     * Set collection of jobs to be evaluated
     * @param jobs List of Job objects
     */
    @Override
    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
        this.jobsAmount = jobs.size();
        this.operationTimes = new Integer[jobsAmount][];
        this.firstNonZeroRow = 0;

        // Build array (easier to work with)
        int i=0;
        for (Job job: jobs) {
            this.operationTimes[i++] = job.getOperationTimes().clone();
        }
    }

    /**
     * Evaluate makespan of the given collection
     * @return Long representing the time needed to finish all jobs
     */
    @Override
    public long evaluate() {
       /*
        Main idea of evaluation algorithm

        We have data like this

        4, 9, 6, 5
        1, 5, 7, 8
        9, 2, 5, 3
        3, 1, 2, 7

        First thing is to start [0,0] operation, which gives us result = 4.
        Now, we have sth like that

        0, 9, 6, 5
        1, 5, 7, 8 		Sum = 4
        9, 2, 5, 3		Available {9,1}
        3, 1, 2, 7

        What does it mean?
        Operation [0,0] is equal to 0 now (it has been ran). Available operations now are 9 and 1 (I don't use
        coordinates here so as not to make a jumble xD).

        How do we find available operations?
        We go from left to right and from top to bottom. First non-zero operation (meaning time) that either
        is in the first row or has 0 operation exactly above itself is available (this means that neither current
        row or current column is occupied by another operation which is of course not allowed in Flow shop).

        Now we just sub the shortest operation from other available operations and it to the result (during the
        shortest time all of available operations work simultaneously). This gives us sth like that

        0, 8, 6, 5
        0, 5, 7, 8 		Sum = 5
        9, 2, 5, 3		Available {8,9}
        3, 1, 2, 7

        Now we just repeat those steps until we have no available operations:

        0, 0, 6, 5
        0, 5, 7, 8 		Sum = 13
        1, 2, 5, 3		Available {6,5,1}
        3, 1, 2, 7

        0, 0, 5, 5
        0, 4, 7, 8 		Sum = 14
        0, 2, 5, 3		Available {5,4,3}
        3, 1, 2, 7

        0, 0, 2, 5
        0, 1, 7, 8 		Sum = 17
        0, 2, 5, 3		Available {2,1}
        0, 1, 2, 7

        0, 0, 1, 5
        0, 0, 7, 8 		Sum = 18
        0, 2, 5, 3		Available {1,2}
        0, 1, 2, 7

        0, 0, 0, 5
        0, 0, 7, 8 		Sum = 19
        0, 1, 5, 3		Available {5,7,1}
        0, 1, 2, 7

        0, 0, 0, 4
        0, 0, 6, 8 		Sum = 20
        0, 0, 5, 3		Available {4,6,1}
        0, 1, 2, 7

        0, 0, 0, 3
        0, 0, 5, 8 		Sum = 21
        0, 0, 5, 3		Available {3,5}
        0, 0, 2, 7

        0, 0, 0, 0
        0, 0, 2, 8 		Sum = 24
        0, 0, 5, 3		Available {2}
        0, 0, 2, 7

        0, 0, 0, 0
        0, 0, 0, 8 		Sum = 26
        0, 0, 5, 3		Available {8,5}
        0, 0, 2, 7

        0, 0, 0, 0
        0, 0, 0, 3 		Sum = 31
        0, 0, 0, 3		Available {3,2}
        0, 0, 2, 7

        0, 0, 0, 0
        0, 0, 0, 1 		Sum = 33
        0, 0, 0, 3		Available {1}
        0, 0, 0, 7

        0, 0, 0, 0
        0, 0, 0, 0 		Sum = 34
        0, 0, 0, 3		Available {3}
        0, 0, 0, 7

        0, 0, 0, 0
        0, 0, 0, 0 		Sum = 37
        0, 0, 0, 0		Available {7}
        0, 0, 0, 7

        0, 0, 0, 0
        0, 0, 0, 0 		Sum = 44
        0, 0, 0, 0		Available {}
        0, 0, 0, 0


        */


        //Let's implement it ^^

        // Take [0,0] element
        long result = operationTimes[0][0];

        // Make it 0
        operationTimes[0][0] = 0;

        List<Coordinates> availableOperations = getAvailableOperations();
        if (availableOperations.isEmpty()) return result;

        Coordinates smallest;
        int smallestValue;

        while (!availableOperations.isEmpty()) {
            // Add the shortest operation to result
            smallest = availableOperations.get(0);
            smallestValue = operationTimes[smallest.getX()][smallest.getY()];
            result += smallestValue;

            // Sub it from every other available operation
            for (Coordinates coord : availableOperations)
                operationTimes[coord.getX()][coord.getY()] -= smallestValue;

            // Get new available operations
            availableOperations = getAvailableOperations();
        }

        return result;
    }

    private List<Coordinates> getAvailableOperations() {
        List<Coordinates> result = new ArrayList<Coordinates>();
        int j;

        for (int i=firstNonZeroRow ; i < operationTimes.length; i++) {
            j = 0;
            while (j < operationTimes[0].length && operationTimes[i][j] == 0) j++;

            // Explanation of this is in the evaluate() method
            if (j < operationTimes[0].length && operationTimes[i][j] != 0
                    && (i == 0 || operationTimes[i-1][j] == 0))
                result.add(new Coordinates(i,j));
            else if (j == operationTimes[0].length) {
                // We just moved through row with all zeros so forget about it ;)
                firstNonZeroRow = i + 1;
            }
        }

        // Let the shortest operation be the first one
        Collections.sort(result);
        return result;
    }

    private class Coordinates implements Comparable<Coordinates>{
        private int x,y;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public int compareTo(Coordinates other) {
            return operationTimes[this.getX()][this.getY()].
                    compareTo(operationTimes[other.getX()][other.getY()]);
        }

        @Override
        public String toString() {
            return "Coordinates: x=" + getX() + ", y=" + getY();
        }
    }
}
