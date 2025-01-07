//[SinErrores]
class Init{
    int x;
    static int y;

    A m1() { return null; }

    void test() {
        m1().x = 1;
        m1().y = 1;

        A.instance().x = 1;
        A.instance().y = 1;

        new A().x = 1;
        new A().y = 1;

        (new A()).x = 1;
        (new A()).y = 1;

        this.x = 1;
        this.y = 1;
    }

    static void main() {}
}

class A {
    int x;
    static int y;
    static A instance() { return new A(); }
}
