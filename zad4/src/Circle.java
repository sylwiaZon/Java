public class Circle extends Shape {
    public static int n;
    public void draw(){
        for (int i = -n; i <= n; i++) {
            for (int j = -n; j <= n; j++) {
                if (i*i + j*j <= n*n) System.out.print("* ");
                else                  System.out.print(". ");
            }
            System.out.println();
        }
    }
    public Circle(int _n){
        n = _n;
    }
}
