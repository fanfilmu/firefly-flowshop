package pl.agh.bo.flowshop;

import pl.agh.bo.flowshop.evaluator.MakespanEvaluator;
import pl.agh.bo.flowshop.solver.Solver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
    private JTextField lightAbsorptionTV;
    private JButton chooseFileWithDataButton;
    private JButton startAlgorithmButton;
    private JCheckBox NEHCheckBox;
    private JCheckBox CDSCheckBox;
    private JRadioButton PMXRadioButton;
    private JRadioButton swapRadioButton;
    private JLabel errorLabel;
    private String maxIterations;
    private String populationSize;
    private String absorbtionCoefficient;
    private String baseAttraction;
    private String lightAbsorption;
    private boolean usesNEH;
    private boolean usesCDS;
    private String path = "";
    private String crossoverStrategy = "PMX";

    public Main() {
        chooseFileWithDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open file chooser form
                final JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(Main.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    // Assign path to a file chosen by user
                    path = fc.getSelectedFile().getAbsolutePath();
                }
            }
        });
        startAlgorithmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Start algorithm

                // Gather data from fields
                getData(Main.this);

                // Check if everything has been filled properly
                if (checkData()) {
                    startAlgorithm();
                    errorLabel.setText("");
                } else {
                    errorLabel.setText("Fill the fields properly! Don't forget about file path ;)");
                }
            }
        });

        PMXRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crossoverStrategy = "PMX";
            }
        });
        swapRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crossoverStrategy = "swap";
            }
        });
    }

    private boolean checkData() {
        boolean result = false;

        // Test if everything is filled
        if (!maxIterations.equals("") && !populationSize.equals("") && !absorbtionCoefficient.equals("")
                && !baseAttraction.equals("") && !lightAbsorption.equals("") && !path.equals(""))
            result = true;

        // Try converting (if anything fails (user didn't fill tv properly) return false)
        try {
            Long.parseLong(maxIterations);
            Long.parseLong(populationSize);
            Double.parseDouble(absorbtionCoefficient);
            Double.parseDouble(baseAttraction);
            Double.parseDouble(lightAbsorption);
        } catch (NumberFormatException e) {
            return false;
        }

        return result;

    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        Main main = new Main();
        // Set cross-platform Java L&F (also called "Metal")
        UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        JFrame frame = new JFrame("Main");
        frame.setContentPane(new Main().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public String getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(final String maxIterations) {
        this.maxIterations = maxIterations;
    }

    public String getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(final String populationSize) {
        this.populationSize = populationSize;
    }

    public String getAbsorbtionCoefficient() {
        return absorbtionCoefficient;
    }

    public void setAbsorbtionCoefficient(final String absorbtionCoefficient) {
        this.absorbtionCoefficient = absorbtionCoefficient;
    }

    public String getBaseAttraction() {
        return baseAttraction;
    }

    public void setBaseAttraction(final String baseAttraction) {
        this.baseAttraction = baseAttraction;
    }

    public String getLightAbsorption() {
        return lightAbsorption;
    }

    public void setLightAbsorption(final String lightAbsorption) {
        this.lightAbsorption = lightAbsorption;
    }

    public boolean isUsesNEH() {
        return usesNEH;
    }

    public void setUsesNEH(final boolean usesNEH) {
        this.usesNEH = usesNEH;
    }

    public boolean isUsesCDS() {
        return usesCDS;
    }

    public void setUsesCDS(final boolean usesCDS) {
        this.usesCDS = usesCDS;
    }

    public void setData(Main data) {
        maxIterationsTV.setText(data.getMaxIterations());
        populationSizeTV.setText(data.getPopulationSize());
        absorbtionCoefficientTV.setText(data.getAbsorbtionCoefficient());
        baseAttractionTV.setText(data.getBaseAttraction());
        lightAbsorptionTV.setText(data.getLightAbsorption());
        NEHCheckBox.setSelected(data.isUsesNEH());
        CDSCheckBox.setSelected(data.isUsesCDS());
    }

    public void getData(Main data) {
        data.setMaxIterations(maxIterationsTV.getText());
        data.setPopulationSize(populationSizeTV.getText());
        data.setAbsorbtionCoefficient(absorbtionCoefficientTV.getText());
        data.setBaseAttraction(baseAttractionTV.getText());
        data.setLightAbsorption(lightAbsorptionTV.getText());
        data.setUsesNEH(NEHCheckBox.isSelected());
        data.setUsesCDS(CDSCheckBox.isSelected());
    }

    public void startAlgorithm() {
        System.out.println("Dobry.");
        List<Job> jobs;
        StringBuilder defaultCombinationResult = new StringBuilder("DEFAULT COMBINATION\n\n");
        StringBuilder ourCombinationResult = new StringBuilder("SOLVER COMBINATION\n\n");

        try {
            InputParser parser = new InputParser(path);
            parser.parse();

            defaultCombinationResult.append("Jobs:\n");
            jobs = parser.getJobs().get(0);
            for (Job job: jobs)
                defaultCombinationResult.append(job + "\n");
            MakespanEvaluator ev = new MakespanEvaluator();
            ev.setJobs(jobs);
            defaultCombinationResult.append("\nThis needs time equal to: " + ev.evaluate());

            Solver algo = new Solver(jobs.toArray(new Job[jobs.size()]), Long.valueOf(maxIterations),
                    Long.valueOf(populationSize),
                    Double.valueOf(lightAbsorption), Double.valueOf(baseAttraction), crossoverStrategy, usesCDS, usesNEH);
            Firefly result = algo.run(Long.parseLong(String.valueOf(parser.getInitialSeed())));

            ourCombinationResult.append("Jobs:\n");
            for (Job job: result.getJobsDistribution())
                ourCombinationResult.append(job + "\n");

            ev.setJobs(Arrays.asList(result.getJobsDistribution()));
            ourCombinationResult.append("\nThis needs time equal to: " + ev.evaluate());

            // Run results dialog
            Results results = new Results(defaultCombinationResult.toString(), ourCombinationResult.toString());

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
