package com.nineplusten.app.service;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
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
	 * @throws IOException 
     */
    public void createPieChart(PieDataset data) throws IOException {

        JFreeChart chart = ChartFactory.createPieChart(
            "Age Distribution",
            data,
            true, // legend
            true, // tooltips
            false // URLs
        );
        PiePlot plot = (PiePlot) chart.getPlot();
        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
            plot.setLabelGenerator(gen);
        
        int width = 640;    /* Width of the image */
        int height = 480;   /* Height of the image */ 
        File pieChart = new File("./reports/pieChart.jpg");
        pieChart.createNewFile();
        ChartUtils.saveChartAsJPEG(pieChart ,chart, width ,height);

    }

}