///[Error:super|13]
class Init{
    static void main() {
    }
}

class A {
   int x;
}

class B extends A {
    void m1() {
        super.x;
    }
}
