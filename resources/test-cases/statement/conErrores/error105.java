///[Error:c|12]
class Init {
    static void main() {}
}

class A {
    private char c;
}

class B extends A {
    public B() {
        super.c = 'c';
    }
}
