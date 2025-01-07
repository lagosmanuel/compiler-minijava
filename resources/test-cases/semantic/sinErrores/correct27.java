///[SinErrores]
class A<T> {
    void m1(T t) {}
}

class B<V> extends A<V> {
    void m1(V t) {}
}

class Init{
    static void main()
    { }
}
