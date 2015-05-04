package pl.agh.bo.flowshop;

/**
 * Created by Andrzej on 2015-04-20.
 */
public class Job {

    private Integer[] operationTimes;
    private int id;

    public Job(int id, Integer[] operationTimes) {
        this.id = id;
        this.operationTimes = operationTimes;
    }

    public Integer[] getOperationTimes() {
        return operationTimes;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ID: " + id  + ", operations: ");

        for (int i : operationTimes) {
            sb.append(i + ",");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Job job = (Job) o;

        if (id != job.id) return false;

        return true;
    }
}
