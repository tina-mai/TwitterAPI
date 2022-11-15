import org.math.plot.*;

import javax.swing.*;
import java.util.ArrayList;

public class Plot {

    private double[] x;
    private double[] y;

    public Plot(ArrayList<Integer> times, ArrayList<Integer> likes) {
        x = new double[times.size()];
        y = new double[likes.size()];

        for (int i=0; i<times.size(); i++) {
            x[i] = (double)(times.get(i));
            y[i] = (double)(likes.get(i));
        }

        plot(x, y);
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

        // put the PlotPanel in a JFrame, as a JPanel
        JFrame frame = new JFrame("a plot panel");
        frame.setContentPane(plot);
        frame.setVisible(true);
    }
}