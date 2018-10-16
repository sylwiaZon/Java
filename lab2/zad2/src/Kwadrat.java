public class Kwadrat {
    double a;
    public Kwadrat(double _a) {
        a = _a;
    }
    double getA(){
        return a;
    }
    void setA(double _a) {
        a = _a;
    }
    double area(){
        return a*a;
    }
    boolean isBigger(Kwadrat b) {
        return a*a < b.a*b.a ? true : false;
    }
}
