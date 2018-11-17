import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class MyPanel extends JPanel implements MouseListener, MouseMotionListener {
    MouseEvent prev_event;
    Shape currentShape;
    public MyPanel() {
        currentShape = null;
        addMouseListener(this);
        addMouseMotionListener(this);
        setPreferredSize(new Dimension(400, 400));
        shapes = new ArrayList<>();
    }
    public ArrayList<Shape> shapes;
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Shape shape: shapes){
            shape.draw(g);
        }
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        if(currentShape != null) {
            double moved_x = e.getX() - prev_event.getX();
            double moved_y = e.getY() - prev_event.getY();
            currentShape.x = currentShape.x + moved_x;
            currentShape.y = currentShape.y + moved_y;
            repaint();
        }
        prev_event = e;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        prev_event = e;
        currentShape = whichShape(e.getX(),e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        prev_event = null;
        currentShape = null;
    }

    @Override
    public void mouseMoved(MouseEvent e) { }

    private Shape whichShape(double x, double y){
        for(Shape shape: shapes){
            if(shape.isInside(x,y) != null){
                return shape;
            }
        }
        return null;
    }
}