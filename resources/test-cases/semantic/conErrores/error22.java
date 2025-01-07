///[Error:m1|4]
abstract class B {
    void m1(A<System> a) {}
    abstract void m1(A<Object> a);
}

class A<T> {

}

class Init{
    static void main()
    { }
}
