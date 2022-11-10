import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

//Extends JPanel class
public class Plot extends JPanel{
    //initialize coordinates
    int[] cord = {0, 20, 40, 60, 80};
    int[] x_cord = {0, 5, 10, 12, 15};
    int marg = 60;

    protected void paintComponent(Graphics grf){
        //create instance of the Graphics to use its methods
        super.paintComponent(grf);
        Graphics2D graph = (Graphics2D)grf;

        //Sets the value of a single preference for the rendering algorithms.
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // get width and height
        int width = getWidth();
        int height = getHeight();

        // draw graph
        graph.draw(new Line2D.Double(marg, marg, marg, height-marg));
        graph.draw(new Line2D.Double(marg, height-marg, width-marg, height-marg));

        //find value of x and scale to plot points
        double x = (double)(width-2*marg)/(cord.length-1);
        double scale = (double)(height-2*marg)/getMax();
        //double xScale = (double)(width-)

        //set color for points
        graph.setPaint(Color.RED);

        // set points to the graph
        for(int i=0; i<cord.length; i++){
        double x1 = width - marg - x_cord[i]; // weird things going on with the x, maybe play around with this
        double y1 = height-marg-scale*cord[i];
        graph.fill(new Ellipse2D.Double(x1-2, y1-2, 6, 6));
        graph.drawString("hello", 10,10);
        }
    }

    // create getMax() method to find maximum value
    private int getMax() {
    int max = -Integer.MAX_VALUE;
    for (int i=0; i<cord.length; i++) {
        if (cord[i]>max)
            max = cord[i];
        }
    return max;
    }

    // main() method start
    public static void main(String args[]) {
        //create an instance of JFrame class
        JFrame frame = new JFrame();
        // set size, layout and location for frame.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Plot());
        frame.setSize(400, 400);
        frame.setLocation(200, 200);
        frame.setVisible(true);
    }
}