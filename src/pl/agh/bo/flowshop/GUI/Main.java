package pl.agh.bo.flowshop.GUI;

import pl.agh.bo.flowshop.InputParser;
import pl.agh.bo.flowshop.movement.MovementStrategyType;
import pl.agh.bo.flowshop.problem.FlowshopProblem;
import pl.agh.bo.flowshop.solution.FlowshopSolution;
import pl.agh.bo.flowshop.solution.VectorFlowshopSolution;
import pl.agh.bo.flowshop.solver.Solver;
import pl.agh.bo.flowshop.solver.SolverListener;
import pl.agh.bo.flowshop.solver.SolverParameters;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by fan on 20.04.15.
 */
public class Main extends Component {
    private JLabel titleLabel;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTextField maxIterationsTV;
    private JTextField populationSizeTV;
    private JTextField absorbtionCoefficientTV;
    private JTextField baseAttractionTV;
    private JButton chooseFileWithDataButton;
    private JButton startAlgorithmButton;
    private JCheckBox NEHCheckBox;
    private JCheckBox CDSCheckBox;
    private JRadioButton PMXRadioButton;
    private JRadioButton swapRadioButton;
    private JLabel errorLabel;
    private SolverParameters parameters;
    private String path = "";

    public Main() {
        initializeSolverParameters();

        chooseFileWithDataButton.addActionListener(e -> {
            // Open file chooser form
            final JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(Main.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                // Assign path to a file chosen by user
                path = fc.getSelectedFile().getAbsolutePath();
            }
        });
        startAlgorithmButton.addActionListener(e -> {
            // Gather data from fields
            updateSolverParameters();

            if (path.equals(""))
                errorLabel.setText("Fill the file path");
            else {
                errorLabel.setText("");
                startAlgorithm();
            }
        });

        PMXRadioButton.addActionListener(e -> parameters.movementStrategy = MovementStrategyType.PMX);
        swapRadioButton.addActionListener(e -> parameters.movementStrategy = MovementStrategyType.SWAP);
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        Main main = new Main();
        // Set cross-platform Java L&F (also called "Metal")
        UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        JFrame frame = new JFrame("Main");
        frame.setContentPane(new Main().mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public void startAlgorithm() {
        FlowshopProblem problem;

        try {
            final InputParser parser = new InputParser(path);
            parser.parse();

            problem = parser.getProblems().get(0);

            FlowshopSolution initial = new VectorFlowshopSolution(problem.jobCount);
            for (int i = 0; i < problem.jobCount; i++) initial.set(i, i);

            Solver solver = new Solver(problem, parameters);

            // Initialize chart displaying results
            final ResultsChart resultsChart = new ResultsChart("Firefly algorithm");
            resultsChart.display(String.valueOf(parameters.maxIterations), String.valueOf(parameters.populationSize),
                    String.valueOf(parameters.absorptionCoefficient),
                    String.valueOf(parameters.baseAttraction), parameters.movementStrategy.toString(),
                    String.valueOf(parameters.useCds), String.valueOf(parameters.useNeh));

            solver.setListener(new SolverListener() {
                @Override
                public void onIterationFinished(int i, long best) {
                    resultsChart.addResult(i, best);
                }

                @Override
                public void onSolverFinished(FlowshopSolution bestSolution, double elapsedTime) {
                    resultsChart.showResults(bestSolution, elapsedTime);
                }
            });

            // Run the algorithm
            new Thread(new Runnable() {
                @Override
                public void run() {
                    solver.run();
                }
            }).start();

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeSolverParameters() {
        parameters = new SolverParameters();

        maxIterationsTV.setText("200");
        populationSizeTV.setText("20");
        absorbtionCoefficientTV.setText("0.1");
        baseAttractionTV.setText("0.1");
        NEHCheckBox.setSelected(false);
        CDSCheckBox.setSelected(false);
    }
    private void updateSolverParameters() {
        parameters.maxIterations = tryIntParse(maxIterationsTV.getText(), 200);
        parameters.populationSize = tryIntParse(populationSizeTV.getText(), 20);
        parameters.absorptionCoefficient = tryFloatParse(absorbtionCoefficientTV.getText(), 0.1f);
        parameters.baseAttraction = tryFloatParse(baseAttractionTV.getText(), 0.1f);
        parameters.useNeh = NEHCheckBox.isSelected();
        parameters.useCds = CDSCheckBox.isSelected();
    }

    private Integer tryIntParse(String text, int defaultValue) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private Float tryFloatParse(String text, float defaultValue) {
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

}
