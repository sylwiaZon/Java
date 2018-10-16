import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.LinkedList;

public class Test {
    LinkedList<Prostokąt> figury = new LinkedList<Prostokąt>();
    public static void main(String[] argv){
        Test test = new Test();
        System.out.println("1. Wczytaj prostokąt");
        System.out.println("2. Wyświetl wszystkie prostokąty");
        System.out.println("3. Oblicz sumę pól wszystkich prostokątów");
        System.out.println("4. Zakończ");
        String input = null;
        boolean run = true;
        while (true) {
            input = test.read();
            switch (input) {
                case "1": test.addFigures();
                    break;
                case "2": test.printFigures();
                    break;
                case "3": test.area();
                    break;
                case "4": System.exit(0);
                    break;
            }
        }
    }
    public void addFigures(){
        String a = read();
        String b = read();
        double _a = (double) a.charAt(0) - '0';
        double _b = (double) b.charAt(0) - '0';
        Prostokąt prostokąt = new Prostokąt(_a, _b);
        figury.add(prostokąt);
    }
    public void printFigures(){
        for (int i = 0; i < figury.size(); i++) {
            System.out.print(figury.get(i).a);
            System.out.print(' ');
            System.out.print(figury.get(i).b);
            System.out.println();
        }
    }
    public void area() {
        double ar = 0;
        for (int i = 0; i < figury.size(); i++) {
            ar = ar + figury.get(i).area();
        }
        System.out.println(ar);
    }
    public String read(){
        String input = null;
        try
        {
            InputStreamReader rd = new InputStreamReader(System.in);
            BufferedReader bfr = new BufferedReader(rd);
            input = bfr.readLine();
        } catch(IOException e){e.printStackTrace();}
        return input;
    }
}
