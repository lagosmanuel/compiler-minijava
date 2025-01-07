//[SinErrores]
class Init{
    static void main() {
        A a = new A();
        a.x = 1;
        a.y = 1;
        a.m1();
        a.m2();
        char c = a.m2(1, new String());
    }
}

class A {
    int x;
    static int y;
    static void m1() {

    }
    void m2() {

    }
    char m2(int x, Object o) {
        return 'c';
    }
}
