package com.nineplusten.app.service;

import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils; 
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
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
        File barChart = new File("./reports/barChart.jpg");
        ChartUtils.saveChartAsJPEG(barChart ,chart, width ,height);

        
    }

}

