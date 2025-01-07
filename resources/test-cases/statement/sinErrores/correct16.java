//[SinErrores]
class Init{
    static void main()
    { }
}

class A {
    int x;
    static int y;
    void m1() {}
    int m2() { return 0; }
}

class B extends A {
    public B() {
       super();
    }
    void m1() {
        super.x = 1;
        super.m1();
    }
}
