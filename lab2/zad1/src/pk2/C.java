package pk2;

import pk1.B;

public class C extends B {
    String name;

    public C(int _number, String _name) {
        super(_number, _name);
    }
    void changeName(String _name) {
        name = _name;
    }
}
