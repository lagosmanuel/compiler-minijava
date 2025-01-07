///[Error:super|14]
class Init {
    static void main() {}
}

class A {
    private A(int i) {

    }
}

class B extends A {
    public B() {
        super(0);
    }
}
