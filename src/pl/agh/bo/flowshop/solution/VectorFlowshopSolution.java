package pl.agh.bo.flowshop.solution;

import java.util.Arrays;

public class VectorFlowshopSolution implements FlowshopSolution {
    private int[] order;

    public VectorFlowshopSolution(int jobCount) {
        this.order = new int[jobCount];
        Arrays.fill(order, -1);
    }

    @Override
    public int get(int index) {
        return order[index];
    }

    @Override
    public void set(int index, int jobId) {
        order[index] = jobId;
    }

    @Override
    public void swap(int position1, int position2) {
        int temp = order[position1];
        order[position1] = order[position2];
        order[position2] = temp;
    }

    @Override
    public int[] getOrder() {
        return order;
    }

    @Override
    public int getLength() {
        return order.length;
    }

    @Override
    public VectorFlowshopSolution getNewInstance() {
        VectorFlowshopSolution result = new VectorFlowshopSolution(order.length);
        for (int i = 0; i < order.length; i++)
            result.set(i, -1);

        return result;
    }

    @Override
    public FlowshopSolution clone() {
        VectorFlowshopSolution result = new VectorFlowshopSolution(order.length);
        for (int i = 0; i < order.length; i++)
            result.set(i, get(i));

        return result;
    }
}
