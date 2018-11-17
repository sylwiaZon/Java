import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Elipse extends Shape {
    public double r,q;
    public Color color;
    public void draw(Graphics g){
        Graphics2D graphics = (Graphics2D) g;
        Ellipse2D ellipse2D = new Ellipse2D.Double(x,y,r*2,q*2);
        graphics.setPaint(color);
        graphics.fill(ellipse2D);
    }
    public Elipse(double _x, double _y, double _r, double _q, Color _color){
        color = _color;
        r = _r;
        q = _q;
        x = _x;
        y = _y;
        name = "Circle";
    }
    public Shape isInside(double _x, double _y){
        double center_x = x + r;
        double center_y = y + q;
        if(((center_x - _x) / r )*((center_x - _x) / r ) + ((center_y - _y) / r )*((center_y - _y) / r ) <= 1 ){
            return this;
        }
        return null;
    }
}
