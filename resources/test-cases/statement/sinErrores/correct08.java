//[SinErrores]
class Init{
    static void main() {
        A a = new A();
        B b = new B();
        boolean c1 = a == b;
        boolean c2 = b == a;
    }
}

class A {

}

class B extends A {

}