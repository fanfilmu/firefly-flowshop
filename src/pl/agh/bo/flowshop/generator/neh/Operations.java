package pl.agh.bo.flowshop.generator.neh;

import pl.agh.bo.flowshop.Job;

public class Operations {

    private static boolean lesserThan(Job job1, Job job2) {

        int i = 0, j = 0;

        for (Integer integer : job1.getOperationTimes()) {
            i += integer;
        }

        for (Integer integer : job2.getOperationTimes()) {
            j += integer;
        }

        return i < j;
    }

    private static boolean greaterThan(Job job1, Job job2) {

        int i = 0, j = 0;

        for (Integer integer : job1.getOperationTimes()) {
            i += integer;
        }

        for (Integer integer : job2.getOperationTimes()) {
            j += integer;
        }

        return i > j;
    }

    public static void swap(Job[] jobs, int first, int second) {
        Job tmp = jobs[first];
        jobs[first] = jobs[second];
        jobs[second] = tmp;
    }

    private static int partition(Job arr[], int left, int right) {

        int i = left, j = right;

        Job pivot = arr[(left + right) / 2];

        while (i <= j) {

            while (lesserThan(arr[i], pivot))
                i++;

            while (greaterThan(arr[j], pivot))
                j--;

            if (i <= j) {
                swap(arr, i, j);
                //tmp = arr[i];
                //arr[i] = arr[j];
                //arr[j] = tmp;
                i++;
                j--;
            }
        }

        return i;
    }

    public static void quickSort(Job[] jobs, int left, int right) {

        int index = partition(jobs, left, right);

        if (left < index - 1)
            quickSort(jobs, left, index - 1);

        if (index < right)
            quickSort(jobs, index, right);
    }

}