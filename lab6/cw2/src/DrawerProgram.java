public class DrawerProgram {
    public static void main(String[] argv){
        double []parameters = new double[4];
        double samples = 0.00001;
        double x1 = -10;
        double x2 = 10;
        parameters[0] = 2;
        parameters[1] = 2;
        parameters[2] = -1;
        parameters[3] = -4;
        MyFrame myFrame = new MyFrame(parameters, x1, x2, samples);
    }
}

