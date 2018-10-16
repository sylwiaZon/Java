public class Prostokąt extends Kwadrat {
    double b;
    public Prostokąt(double _a, double _b) {
        super(_a);
        b = _b;
    }
    void setB(double _b){
        b = _b;
    }
    double getB(){
        return b;
    }
    double area(){
        return a*b;
    }
    boolean isBigger(Prostokąt prostokąt) {
        return a*b < prostokąt.a*prostokąt.b ? true:false;
    }
}

