//[SinErrores]
class Init{
    void m1() {}
    static void m2() {}

    void test() {
        m1();
        m2();
        new A();
        new A();
        A.m2();

        A a = new A();
        a.m1();
        a.m2();
        a.b.m1();
        a.b.m2();
    }

    static void main() {}
}

class A {
    B b;
    void m1() {}
    static void m2() {}
}

class B {
    void m1() {}
    static void m2() {}
}

