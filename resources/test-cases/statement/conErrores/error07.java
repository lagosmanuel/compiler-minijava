///[Error:m2|4]
class Init{
    static void main() {
        m1().b.m2();
    }

    static A m1() {
        return new A();
    }
}

class A {
   B b;
}

class B {

}
