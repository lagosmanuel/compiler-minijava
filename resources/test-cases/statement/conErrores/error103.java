///[Error:x|13]
class Init {
    static void main() {
    }
}

class A {
    int x;
}

class B extends A {
    public B() {
        super().x = 10;
    }
}
