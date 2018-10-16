package pk1;

import pk2.C;

public class Program {
    public static void main(String [] argv) {
        B b = new B(1, "b");
        A a = new A(2, "a");
        System.out.println(b.number);
        System.out.println(a.number);
        b.callIncrement();
        a.callIncrement();
        System.out.println(b.number);
        System.out.println(a.number);
        C c = new C(1, "c");
        System.out.println(c.number);
        c.callIncrement();
        System.out.println(c.number);
    }
}
