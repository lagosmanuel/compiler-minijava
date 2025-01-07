///[SinErrores]
abstract class A{
    abstract void m1();
}

abstract class B extends A{
    abstract void m2();
}

class C extends B {
    void m1() {}
    void m2() {}
}

class Init{
    static void main()
    { }
}
