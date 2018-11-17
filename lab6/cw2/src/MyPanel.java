import javax.swing.*;
import java.awt.*;

public class MyPanel extends JPanel {
    Graph graph;
    public MyPanel(double []parameters, double x1, double x2, double samples) {
        setPreferredSize(new Dimension(400, 400));
        graph = new Graph(parameters,0.01,x1,x2);
    }
    @Override
    protected void paintComponent(Graphics g) {
        graph.draw(g);
    }

}