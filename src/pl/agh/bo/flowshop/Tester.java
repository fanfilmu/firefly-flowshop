package pl.agh.bo.flowshop;


import pl.agh.bo.flowshop.movement.MovementStrategyType;
import pl.agh.bo.flowshop.problem.FlowshopProblem;
import pl.agh.bo.flowshop.solution.FlowshopSolution;
import pl.agh.bo.flowshop.solver.Solver;
import pl.agh.bo.flowshop.solver.SolverListener;
import pl.agh.bo.flowshop.solver.SolverParameters;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

public class Tester {
    public static void main(String args[]) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("retries count:");
        int retriesCount = scanner.nextInt();

        System.out.println("params set count:");
        int setCount = scanner.nextInt();

        System.out.println("problem:");
        String path = scanner.next();

        InputParser parser = new InputParser(path);
        parser.parse();
        FlowshopProblem problem = parser.getProblems().get(0);

        LinkedList<SolverParameters> paramList = new LinkedList<>();
        for (int i = 0; i < setCount; i++) {
            System.out.println("maxIterations populationSize absorptionCoefficient baseAttraction useCds(true/false) useNeh(true/false)");
            SolverParameters params = new SolverParameters();
            params.maxIterations = scanner.nextInt();
            params.populationSize = scanner.nextInt();
            params.absorptionCoefficient = Float.parseFloat(scanner.next());
            params.baseAttraction = Float.parseFloat(scanner.next());
            params.useCds = scanner.nextBoolean();
            params.useNeh = scanner.nextBoolean();

            System.out.println("Use PMX? (Swap if false)");
            if (scanner.nextBoolean())
                params.movementStrategy = MovementStrategyType.PMX;
            else
                params.movementStrategy = MovementStrategyType.SWAP;

            paramList.push(params);
        }

        for (SolverParameters paramSet : paramList) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd_HH-mm-ss.SSS");
            PrintWriter writer = new PrintWriter(String.format("%s.csv", sdf.format(new Date())), "UTF-8");

            writer.write(String.format("\"iterations: %d, pop size: %d, absorption coeff: %f, baseAttraction: %f, cds: %b, neh: %b, movement: %s\"\n",
                    paramSet.maxIterations, paramSet.populationSize, paramSet.absorptionCoefficient, paramSet.baseAttraction,
                    paramSet.useCds, paramSet.useNeh, paramSet.movementStrategy.toString()));

            SolverListener listener = new SolverListener() {
                @Override
                public void onIterationFinished(int i, long best) {
                    writer.write(String.valueOf(best));
                    writer.write(",");
                }

                @Override
                public void onSolverFinished(FlowshopSolution bestSolution, double elapsedTime) {
                    writer.write("\n");
                }
            };

            for (int i = 0; i < retriesCount; i++) {
                Solver solver = new Solver(problem, paramSet);
                solver.setListener(listener);
                solver.run();
            }

            writer.close();
        }
    }
}
