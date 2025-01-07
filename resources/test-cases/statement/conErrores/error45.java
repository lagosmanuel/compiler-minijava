///[Error:x|4]
class Init{
    static void main() {
        new B().x = 0;
    }
}

class A {
    private int x;
}

class B extends A {

}
