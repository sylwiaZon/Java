import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Rectangle  extends Shape {
    public double a, b;
    public Color color;
    public void draw(Graphics g){
        Graphics2D graphics = (Graphics2D) g;
        Rectangle2D rectangle2D = new Rectangle2D.Double(x,y,a,b);
        graphics.setPaint(color);
        graphics.fill(rectangle2D);
    }
    public Rectangle(double _x, double _y, double _a, double _b, Color _color){
        color = _color;
        x = _x;
        y = _y;
        a = _a;
        b = _b;
        name = "Square";
    }
    public Shape isInside (double _x, double _y){
        if(_x - x <= a && _x - x >= 0 && _y - y <= b && _y - y >=0){
            return this;
        }
        return null;
    }
}
