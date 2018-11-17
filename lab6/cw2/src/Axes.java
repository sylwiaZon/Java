import java.awt.*;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;

public class Axes {
    double x1,x2;
    double centerX,centerY, graduation;
    double []yAxisPoint;
    public void draw(Graphics g){
        Graphics2D graphics = (Graphics2D) g;
        graphics.draw(new Line2D.Double(0,centerY,400,centerY));
        graphics.draw(new Line2D.Double(400,centerY,390,centerY - 5));
        graphics.draw(new Line2D.Double(400,centerY,390,centerY + 5));
        graphics.draw(new Line2D.Double(centerX,0,centerX,400));
        graphics.draw(new Line2D.Double(centerX,0,centerX - 5,10));
        graphics.draw(new Line2D.Double(centerX,0,centerX + 5,10));
        makeGraduation(graphics);
    }
    public Axes(double _x1, double _x2, double _graduation, double []_yAxisPoint){
        x1 = _x1;
        x2 = _x2;
        centerX = 10;
        centerY = 390;
        graduation = _graduation;
        yAxisPoint = _yAxisPoint;
    }
    private void makeGraduation(Graphics2D graphics2D){
        double usedGraduation = graduation;
        int usedGraduationIteration = 1;
        if(graduation < 30){
            usedGraduation = 2 * graduation;
            usedGraduationIteration = 2;
        }
        double it = x1 + 1;
        for(double i = graduation; i <= 400 ; i = i + usedGraduation){
            graphics2D.drawString((it) + "",(float)i,380);
            graphics2D.draw(new Line2D.Double(i,centerY - 5,i,centerY + 5));
            it = usedGraduationIteration + it;
        }
        double itDown = yAxisPoint[0];
        double itUp = yAxisPoint[0];
        double j = yAxisPoint[1];
        for(double i = yAxisPoint[1]; i < 400 || j > 0; i = i + usedGraduation, j = j - usedGraduation){
            graphics2D.drawString(new DecimalFormat("##.##").format(itDown) + "",20,(float)i);
            graphics2D.draw(new Line2D.Double(centerX - 5,i, centerX + 5,i));
            graphics2D.drawString(new DecimalFormat("##.##").format(itUp) + "",20,(float)j);
            graphics2D.draw(new Line2D.Double(centerX - 5,j, centerX + 5,j));
            itDown = it - usedGraduationIteration;
            itUp = it + usedGraduationIteration;
        }
    }
}

