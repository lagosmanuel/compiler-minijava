///[Error:m1|4]
class Init{
    static void main() {
        new A().m1();
    }
}

class A {
    private static void m1() { }
}
