import org.math.plot.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Plot {

    private double[] x; // array of times
    private double[] y; // array of likes

    private double[][] morning; // 2D array of the x's and y's for morning times
    private double[][] afternoon; // 2D array of the x's and y's for afternoon times
    private double[][] night; // 2D array of the x's and y's for night times

    public Plot(ArrayList<Integer> times, ArrayList<Integer> likes) {
        x = new double[times.size()];
        y = new double[likes.size()];

        // populate x and y with the arguments that the user inputs
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

        plot(morning, afternoon, night);
    }

    // plot the morning, afternoon, and night arrays set up in the constructor
    public void plot(double[][] morning, double[][] afternoon, double[][] night) {
        Plot2DPanel plot = new Plot2DPanel();

        // add each ScatterPlot of morning, afternoon, and night points to the plot
        plot.addScatterPlot("Morning (4am – 11am)", Color.GREEN, morning);
        plot.addScatterPlot("Afternoon (12pm – 7pm)", Color.RED, afternoon);
        plot.addScatterPlot("Night (8pm – 3am)", Color.BLUE, night);

        // add a legend at the bottom of the screen
        plot.addLegend("SOUTH");

        plot.setAxisLabels("Hour", "Likes");

        plot.setFixedBounds(0, 0, 24);

        // put the PlotPanel in a JFrame, as a JPanel
        JFrame frame = new JFrame("Twitter Word Plot");
        frame.setSize(1000, 600);
        frame.setContentPane(plot);
        frame.setVisible(true);

    }
}
