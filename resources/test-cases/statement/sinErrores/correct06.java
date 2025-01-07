//[SinErrores]

class Test {
    int x;
    static char c;

    void m1() {}
    A m2() {return null;}
    static void m3() {}

    void test(int y) {
        int z;
        m1();
        m2().x = 1;
        m2().m1();
        x = 1;
        y = 1;
        z = 1;
        new A();
        c = 'c';
        m3();
    }
}

class A {
    int x;
    void m1() {
        x = 10;
    }
}

class Init{
    static void main() {
    }
}
