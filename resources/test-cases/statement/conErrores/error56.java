///[Error:=|4]
class Init{
    static void main() {
        new A().m1() = 10;
    }
}

class A {
    int m1() {}
}
