import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Circle extends Shape {
    public double r;
    public Color color;
    public void draw(Graphics g){
        Graphics2D graphics = (Graphics2D) g;
        Ellipse2D ellipse2D = new Ellipse2D.Double(x,y,r*2,r*2);
        graphics.setPaint(color);
        graphics.fill(ellipse2D);
    }
    public Circle(double _x, double _y, double _r, Color _color){
        color = _color;
        r = _r;
        x = _x;
        y = _y;
        name = "Circle";
    }
    public Shape isInside(double _x, double _y){
        double center_x = x + r;
        double center_y = y + r;
        if((center_x - _x)*(center_x - _x) + (center_y - _y)*(center_y - _y) <= r*r ){
            return this;
        }
        return null;
    }
}
