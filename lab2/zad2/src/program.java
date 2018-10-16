public class program {
    public static void main( String[] argv) {
        Kwadrat kw = new Kwadrat(2);
        Kwadrat kw2 = new Kwadrat(3);
        System.out.println(kw2.isBigger(kw));
        System.out.println(kw.area());
    }
}
