package pl.agh.bo.flowshop;

import java.util.List;

/**
 *
 * This class evaluates the amount of time needed to process all jobs in a
 * specific order. Only one operation per job at one time is permitted.
 *
 * Created by Andrzej on 2015-04-20.
 */
public class Evaluator {

    private List<Job> jobs;
    private int jobsAmount;
    private Integer[][] operationTimes;

    public Evaluator(List<Job> jobs) {
        this.jobs = jobs;
        this.jobsAmount = jobs.size();
        this.operationTimes = new Integer[jobsAmount][];

        // Build array (easier to work with)
        int i=0;
        for (Job job: jobs)
            this.operationTimes[i++] = job.getOperationTimes().clone();
    }

    public long evaluate() {
        // Main idea of evaluation algorithm
        //
        // We have data like this
        //
        // ID: 1, operations: 4,9,6
        // ID: 2, operations: 3,3,9
        // ID: 3, operations: 5,1,9
        //
        // First thing is to add [0,0] and make it equal 0
        //
        // ID: 1, operations: 0,9,6
        // ID: 2, operations: 3,3,9     Sum = 4
        // ID: 3, operations: 5,1,9
        //
        // Now we look at first number under 0 and first number to the right of leading 0
        // Whichever is lesser we add it to the sum and make it 0. We have to sub it from
        // the other number, because those operations will run simultaneously
        //
        // ID: 1, operations: 0,6,6
        // ID: 2, operations: 0,3,9     Sum = 7
        // ID: 3, operations: 5,1,9
        //
        // Now we just repeat those steps
        //
        // ID: 1, operations: 0,1,6
        // ID: 2, operations: 0,3,9     Sum = 12
        // ID: 3, operations: 0,1,9
        //
        // ID: 1, operations: 0,0,6
        // ID: 2, operations: 0,3,9     Sum = 13
        // ID: 3, operations: 0,1,9
        //
        // ID: 1, operations: 0,0,3
        // ID: 2, operations: 0,0,9     Sum = 16
        // ID: 3, operations: 0,1,9
        //
        // ID: 1, operations: 0,0,2
        // ID: 2, operations: 0,0,9     Sum = 17
        // ID: 3, operations: 0,0,9
        //
        // ID: 1, operations: 0,0,0
        // ID: 2, operations: 0,0,9     Sum = 19
        // ID: 3, operations: 0,0,9
        //
        // ID: 1, operations: 0,0,0
        // ID: 2, operations: 0,0,0     Sum = 28
        // ID: 3, operations: 0,0,9
        //
        // ID: 1, operations: 0,0,0
        // ID: 2, operations: 0,0,0     Sum = 37
        // ID: 3, operations: 0,0,0
        //

        // Let's implement it ^^

        // Take [0,0] element
        long result = operationTimes[0][0];

        // Make it 0
        operationTimes[0][0] = 0;

        Integer[] firstUnderCoord = new Integer[]{1,0};
        int firstUnder = operationTimes[firstUnderCoord[0]][firstUnderCoord[1]];
        Integer[] firstToRightOfCoord = new Integer[]{0,1};
        int firstToRightOf = operationTimes[firstToRightOfCoord[0]][firstToRightOfCoord[1]];

        while (firstUnder != 0 || firstToRightOf != 0) {
            if (firstToRightOfCoord == null) {
                result += firstUnder;

                // Clear under element
                operationTimes[firstUnderCoord[0]][firstUnderCoord[1]] = 0;
            } else if (firstUnderCoord == null) {
                result += firstToRightOf;

                // Clear toRightOf element
                operationTimes[firstToRightOfCoord[0]][firstToRightOfCoord[1]] = 0;
            } else if (firstUnder < firstToRightOf) {
                result += firstUnder;

                // Sub smaller one
                operationTimes[firstUnderCoord[0]][firstUnderCoord[1]] = 0;
                operationTimes[firstToRightOfCoord[0]][firstToRightOfCoord[1]] -= firstUnder;
            } else if (firstUnder > firstToRightOf) {
                result += firstToRightOf;

                // Sub smaller one
                operationTimes[firstUnderCoord[0]][firstUnderCoord[1]] -= firstToRightOf;
                operationTimes[firstToRightOfCoord[0]][firstToRightOfCoord[1]] = 0;
            } else {
                result += firstUnder;
                operationTimes[firstToRightOfCoord[0]][firstToRightOfCoord[1]] = 0;
                operationTimes[firstUnderCoord[0]][firstUnderCoord[1]] = 0;
            }

            // Find new "under" and "rightof" numbers
            firstUnderCoord = firstUnderZero();
            firstToRightOfCoord = firstToRightOfZero();

            // Watch out for coordinates outside of the array
            firstToRightOf = (firstToRightOfCoord == null) ? 0 : operationTimes[firstToRightOfCoord[0]][firstToRightOfCoord[1]];
            firstUnder = (firstUnderCoord == null) ? 0 :operationTimes[firstUnderCoord[0]][firstUnderCoord[1]];

        }

        return result;
    }

    private Integer[] firstUnderZero() {
        Integer[] result = new Integer[2];

        // Returns the coordinates of first element "under zero"

        for (int i=0 ; i < operationTimes[0].length; i++) {
            for (int j = 0; j < operationTimes.length; j++) {
                if (operationTimes[j][i] != 0 && j != 0)
                    return new Integer[]{j,i};
                else if (operationTimes[j][i] != 0 && j == 0)
                    return null;
            }
        }
        return null;
    }

    private Integer[] firstToRightOfZero() {
        Integer[] result = new Integer[2];

        // Returns the coordinates of first element "to right of zero"

        for (int i=0 ; i < operationTimes.length; i++) {
            for (int j = 0; j < operationTimes[0].length; j++) {
                if (operationTimes[i][j] != 0 && j != 0)
                    return new Integer[]{i,j};
                else if (operationTimes[i][j] != 0 && j == 0)
                    return null;
            }
        }
        return null;

    }
}
