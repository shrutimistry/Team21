package com.nineplusten.app.service;

import java.awt.Color;
import java.awt.GradientPaint;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils; 
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;

public class DoubleBarGraph {
	/**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
	 * @throws IOException 
     */
    public void createChart(final CategoryDataset dataset, String title) throws IOException {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
            title,         // chart title
            "",               // domain axis label
            "Percentage",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );

        int width = 640;    /* Width of the image */
        int height = 480;   /* Height of the image */ 
        File barChart = new File( "reports/barChart.png" ); 
        ChartUtils.saveChartAsJPEG(barChart ,chart, width ,height);
       

//        ChartFrame frame = new ChartFrame("First", chart);
//        frame.pack();
//        frame.setVisible(true);
        
    }

}
