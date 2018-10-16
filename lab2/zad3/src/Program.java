public class Program {
    public static void main( String[] argv) {
        Prostokąt prostokąt = new Prostokąt(2, 4);
        Prostokąt prostokąt1 = new Prostokąt(3, 1);
        System.out.println(prostokąt.isBigger(prostokąt1));
        System.out.println(prostokąt.area());
    }
}
