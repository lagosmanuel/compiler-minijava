//[SinErrores]
class Init {
    static void main() {}
}

class A {
    int x;
    private char c;

    public A(int x, char c) {
        this.x = x;
        this.c = c;
    }

    void m1() {

    }

    static void m2() {

    }
}

class B extends A {
    public B() {
        super(1, 'c');
        super.m1();
        super.m2();
        super.x = 0;
    }
}
