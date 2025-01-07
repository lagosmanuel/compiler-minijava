///[Error:C|11]

class A {
    void m1() {}
}

abstract class B extends A {
    abstract void m1();
}

class C extends B {

}

class Init{
    static void main()
    { }
}
