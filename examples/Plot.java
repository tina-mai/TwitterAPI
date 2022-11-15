import org.math.plot.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Plot {

    private double[] x;
    private double[] y;

    private double[][] morning;
    private double[][] afternoon;
    private double[][] night;

    public Plot(ArrayList<Integer> times, ArrayList<Integer> likes) {
        x = new double[times.size()];
        y = new double[likes.size()];

        for (int i=0; i<times.size(); i++) {
            x[i] = (double)(times.get(i));
            y[i] = (double)(likes.get(i));
        }

        // count how many points are morning, afternoon, or night so we can set the size of the arrays (they MUST be arrays and NOT ArrayLists because that's how this plot function works)

        // morning: 4-11 (4am – 11am)
        // afternoon: 12-19 (12pm – 7pm)
        // night: 20-3 (8pm – 3am)

        int mCount = 0;
        int aCount = 0;
        int nCount = 0;

        for (int i=0; i<x.length; i++) {
            if (x[i] >= 4 && x[i] <= 11)
                mCount++;
            if (x[i] >= 12 && x[i] <= 19)
                aCount++;
            if (x[i] >= 20 || x[i] <= 3)
                nCount++;
        }

        // sort x and y values in morning, afternoon, and night 2D arrays
        morning = new double[mCount][2];
        afternoon = new double[aCount][2];
        night = new double[nCount][2];

        int mIndex = 0;
        int aIndex = 0;
        int nIndex = 0;

        for (int i=0; i<x.length; i++) {
            if (x[i] >= 4 && x[i] <= 11) {
                morning[mIndex][0] = x[i];
                morning[mIndex][1] = y[i];
                mIndex++;
            }
            if (x[i] >= 12 && x[i] <= 19) {
                afternoon[aIndex][0] = x[i];
                afternoon[aIndex][1] = y[i];
                aIndex++;
            }
            if (x[i] >= 20 || x[i] <= 3) {
                night[nIndex][0] = x[i];
                night[nIndex][1] = y[i];
                nIndex++;
            }
        }

//        plot(x, y);
        plot(morning, afternoon, night);
    }

//    public static void main(String[] args) {
//        double[] x = {1, 2, 3, 35, 5};
//        double[] y = {1, 10, 3, 4, 5};
//
//        // create your PlotPanel (you can use it as a JPanel)
//        Plot2DPanel plot = new Plot2DPanel();
//
//        // add a line plot to the PlotPanel
////        plot.addLinePlot("my plot", x, y);
//        plot.addScatterPlot("scatter plot", x, y);
//        plot.setAxisLabels("Time", "Likes");
//
//        // put the PlotPanel in a JFrame, as a JPanel
//        JFrame frame = new JFrame("a plot panel");
//        frame.setContentPane(plot);
//        frame.setVisible(true);
//    }

    public void plot(double[] x, double[] y) {

        // create your PlotPanel (you can use it as a JPanel)
        Plot2DPanel plot = new Plot2DPanel();

        // add a line plot to the PlotPanel
//        plot.addLinePlot("my plot", x, y);
        plot.addScatterPlot("scatter plot", x, y);
        plot.setAxisLabels("Hour", "Likes");
        plot.setFixedBounds(0, 0, 24); // set the max of the x-axis to 24 since there are only 24 hours in a day
        // TODO: scaling of x-axis is very weird

        // put the PlotPanel in a JFrame, as a JPanel
        JFrame frame = new JFrame("a plot panel");
        frame.setSize(1000, 600);
        frame.setContentPane(plot);
        frame.setVisible(true);
    }

    public void plot(double[][] morning, double[][] afternoon, double[][] night) {
        Plot2DPanel plot = new Plot2DPanel();

        plot.addScatterPlot("Morning (4am – 11am)", Color.GREEN, morning);
        plot.addScatterPlot("Afternoon (12pm – 7pm)", Color.RED, afternoon);
        plot.addScatterPlot("Night (8pm – 3am)", Color.BLUE, night);

        plot.addLegend("SOUTH");

        plot.setAxisLabels("Hour", "Likes");
        plot.setFixedBounds(0, 0, 24);

        JFrame frame = new JFrame("a plot panel");
        frame.setSize(1000, 600);
        frame.setContentPane(plot);
        frame.setVisible(true);

    }
}