import javax.swing.*;

public class MyFrame extends JFrame {

    public MyFrame(double []parameters, double x1, double x2, double samples) {
        super("Rysowanie");
        JPanel panel = new MyPanel(parameters, x1, x2, samples);
        add(panel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

}