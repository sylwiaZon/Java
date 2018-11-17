import java.awt.*;

public abstract class Shape {
    public String name;
    public double x,y;
    public abstract void draw(Graphics graphics);
    public abstract Shape isInside(double x, double y);
}
