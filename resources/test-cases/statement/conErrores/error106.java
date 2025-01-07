///[Error:m1|12]
class Init {
    static void main() {}
}

class A {
    private void m1() {}
}

class B extends A {
    public B() {
        super.m1();
    }
}
