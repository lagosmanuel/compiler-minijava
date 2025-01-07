///[SinErrores]
abstract class A {
    abstract void m1();
}

abstract class B extends A {
    abstract void m2();
    void m1() {}
}


class Init{
    static void main()
    { }
}
