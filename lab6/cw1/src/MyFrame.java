import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {

    public MyFrame() {
        super("Rysowanie");
        JPanel panel = new MyPanel();
        ((MyPanel) panel).shapes.add(new Circle(0,0,50, Color.BLUE));
        ((MyPanel) panel).shapes.add((new Square(200,100,150, Color.RED)));
        ((MyPanel) panel).shapes.add(new Elipse(0,300,20, 30, Color.CYAN));
        ((MyPanel) panel).shapes.add((new Rectangle(300,0,50, 100, Color.PINK)));
        add(panel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

}