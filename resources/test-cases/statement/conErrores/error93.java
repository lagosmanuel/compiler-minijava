///[Error:m1|15]
class Init{
    static void main() {
    }
}

class A {
    void m1(int i) {

    }
}

class B extends A {
    void m1() {
        super.m1();
    }
}
