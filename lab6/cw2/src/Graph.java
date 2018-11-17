import java.awt.*;
import java.awt.geom.Line2D;
import java.sql.Struct;

public class Graph {
    double []parameters;
    double sample;
    double x1,x2,centerX, centerY;
    double graduation;
    Axes axes;
    public void draw(Graphics g){
        Graphics2D graphics = (Graphics2D) g;
        double startX = 0, endX, startY, endY;
        int it = 1;
        boolean flag = false;
        double []yAxisPoint = new double[2];
        for(double i = x1; i <= x2 - sample; i = i + sample, it++){
            endX = (sample * it) * graduation;
            startY = graduation * (-1) * function(i) + 200;
            endY = graduation * (-1) * function(i + sample) + 200;
            graphics.draw(new Line2D.Double(startX,startY,endX,endY));
            if(startX > 0 && startX < 400 && startY > 0 && startY < 400  && !flag) {
                flag = true;
                yAxisPoint[0] = function(i);
                yAxisPoint[1] = startY;
            }
            startX = endX;
        }
        axes = new Axes(x1,x2, graduation, yAxisPoint);
        axes.draw(g);
    }
    public Graph(double []param, double _sample, double _x1, double _x2){
        parameters = param;
        sample = _sample;
        x1 = _x1;
        x2 = _x2;
        double graduationX = 400 / Math.abs(x1 - x2);
        double yDifference =  Math.abs(function(x1) - function(x2));
        double graduationY;
        if(yDifference == 0){
            graduationY = 400 / function(x2);
        } else {
            graduationY = 400 / Math.abs(function(x1) - function(x2));
        }
        graduation = Math.max(graduationX,graduationY);
        if(x1 < 0 && x2 > 0){
            centerX = (400 / Math.abs(x1 - x2)) *  Math.abs(x1);
        } else {
            centerX = 15;
        }
        centerY = 200;
    }
    public double function(double x){
        double y = 0;
        int pow = parameters.length - 1;
        for(double param: parameters){
            y = y + param * Math.pow(x,pow);
            pow = pow - 1;
        }
        return y;
    }
}

