///[Error:m1|14]
class Init{
    static int m1(A a, B b) {
        return 0;
    }

    static void main() {
        A a = new A();
        B b = new B();

        int i = m1(b, b);
        int j = m1(a, b);

        int n = m1(a, a);
        int m = m1(b, a);
    }
}

class A {

}

class B extends A {

}