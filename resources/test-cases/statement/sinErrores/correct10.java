//[SinErrores]
class Init{
    char m1(int x, char c) {
        return c;
    }

    void m2(Object o) {

    }

    void m1() {}
    static void m2() {}
    String m3() {return new String();}

    void test() {
        char c = m1(1, 'c');
        m2(new String());
        m1();
        m2();
        Object obj = m3();

        new A();
        new A(new String());
        new A(1, 'c');
    }

    static void main() { }
}

class A {
    public A() {

    }

    public A(Object o) {

    }

    public A(int x, char c) {

    }
}
