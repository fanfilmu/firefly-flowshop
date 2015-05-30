package pl.agh.bo.flowshop.GUI;

import java.awt.*;
import java.text.NumberFormat;
import java.util.Arrays;
import javax.swing.*;

import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import pl.agh.bo.flowshop.solution.FlowshopSolution;

/**
 * Created by Andrzej on 2015-05-18.
 */
public class ResultsChart extends JFrame {

    private XYSeries added = new XYSeries("Results", false);
    private XYDataset data;
    private XYPlot xyPlot;
    private final ChartPanel chartPanel;
    private int bestIteration = 0;
    private long bestLightIntensity = Long.MAX_VALUE;
    private double algDuration;
    private String maxIterations;
    private String populationSize;
    private String lightAbsorption;
    private String baseAttraction;
    private String crossoverStrategy;
    private String usesCDS;
    private String usesNEH;

    public ResultsChart(String s) {
        super(s);
        data = createEmptyDataset();
        chartPanel = createResultsPanel();
        this.add(chartPanel, BorderLayout.CENTER);
        JPanel control = new JPanel();
        this.add(control, BorderLayout.SOUTH);
    }

    private ChartPanel createResultsPanel() {
        JFreeChart jfreechart = ChartFactory.createXYLineChart(
                null, "Makespan", "Iterations", data, PlotOrientation.HORIZONTAL, false, false, false);

        xyPlot = (XYPlot) jfreechart.getPlot();
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        return new ChartPanel(jfreechart);
    }

    private XYDataset createEmptyDataset() {
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        xySeriesCollection.addSeries(added);
        return xySeriesCollection;
    }

    public void addResult(int iteration, long lightIntensity) {
        added.add(lightIntensity, iteration);

        // Remember best iteration
        if (lightIntensity < bestLightIntensity) {
            bestLightIntensity = lightIntensity;
            bestIteration = iteration;
        }
    }

    public void display(String maxIterations, String populationSize, String lightAbsorption, String baseAttraction,
                        String crossoverStrategy, String usesCDS, String usesNEH) {
        this.maxIterations = maxIterations;
        this.populationSize = populationSize;
        this.lightAbsorption = lightAbsorption;
        this.baseAttraction = baseAttraction;
        this.crossoverStrategy = crossoverStrategy;
        this.usesCDS = usesCDS;
        this.usesNEH = usesNEH;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                pack();
                setLocationRelativeTo(null);
                setVisible(true);
                setResizable(false);
                setSize(new Dimension(1200, 600));
                setLocation(100,100);
            }
        });

    }

    public void showResults(FlowshopSolution bestOne, double elapsedTime) {
        this.algDuration = elapsedTime;
        final JPanel resultsPanel = new JPanel();
        final JTextPane resultsTextPane = new JTextPane();
        StringBuilder sb = new StringBuilder("");

        sb.append(String.format("\nBest result was found during %d iteration\nIt was " +
                "equal to %d\n\n", bestIteration, bestLightIntensity));

        sb.append(String.format("Running algorithm took %.3f seconds\n\n", algDuration));
        sb.append("This is a solution found by our algorithm:\n");

        for (int jobId : bestOne.getOrder())
            sb.append(jobId + "\n");


        // Append settings
        sb.append("\n\n\nSETTINGS USED IN THIS ALGORITHM:\n\n");

        sb.append(String.format("Max iterations: %s\n", maxIterations));
        sb.append(String.format("Population size: %s\n", populationSize));
        sb.append(String.format("Absorption coefficient: %s\n", lightAbsorption));
        sb.append(String.format("Crossover strategy: %s\n", crossoverStrategy));
        sb.append(String.format("Base attraction: %s\n", baseAttraction));
        sb.append(String.format("Using CDS: %s\n", usesCDS));
        sb.append(String.format("Using NEH: %s\n", usesNEH));

        String resultString = sb.toString();
        resultsTextPane.setText(resultString);
        resultsTextPane.setEditable(false);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                resultsPanel.add(resultsTextPane);
                add(resultsPanel, BorderLayout.EAST);
                invalidate();
                validate();
                repaint();
            }
        });

    }
}