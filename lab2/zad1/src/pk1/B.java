package pk1;

public class B extends A {
    public B(int _number, String _name) {
        super(_number, _name);
    }
    protected void decrement(){
        number = number - 2;
    }
    void changeName(String _name){
        name = _name;
    }
    private void increment(){
        number = number + 2;
    }
}
