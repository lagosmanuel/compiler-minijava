///[Error:x|4]
class Init{
    static void main() {
        new A().x = 0;
    }
}

class A {
    private static int x;
}