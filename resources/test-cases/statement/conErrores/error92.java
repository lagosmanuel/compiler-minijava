///[Error:x|13]
class Init{
    static void main() {
    }
}

class A {
    private int x;
}

class B extends A {
    void m1() {
        super.x = 10;
    }
}
