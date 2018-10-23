public class Square  extends Shape {
    public static int n;
    public void draw(){
        for (int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(i==0 || i ==9){
                    System.out.print("x");
                }
                else if (j==0 || j == 9) {
                    System.out.print("x");
                }
                else{
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }
    public Square(int _n){
        n = _n;
    }
}
