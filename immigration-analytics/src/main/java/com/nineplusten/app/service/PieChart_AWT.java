package com.nineplusten.app.service;

import java.text.DecimalFormat;
import java.util.HashMap;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
 
public class PieChart_AWT  {
   
	/**
     * The starting point for the demo.
     *
     * @param args ignored.
     */
    public void createPiechart(PieDataset data) {
    	// create a dataset...
//        DefaultPieDataset data = new DefaultPieDataset();
//        data.setValue("Category 1", 43.2);
//        data.setValue("Category 2", 27.9);
//        data.setValue("Category 3", 79.5);
        // create a dataset...
        JFreeChart chart = ChartFactory.createPieChart(
            "Age Distribution",
            data,
            true, // legend?
            true, // tooltips?
            false // URLs?
        );
        PiePlot plot = (PiePlot) chart.getPlot();
        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
            plot.setLabelGenerator(gen);
        // create and display a frame...
        ChartFrame frame = new ChartFrame("First", chart);
        frame.pack();
        frame.setVisible(true);
    }

}