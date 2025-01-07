///[SinErrores]
class A<L, T> {
    void m1(T t) {}
}

class B<K> extends A<K, Object> {
    void m1(Object t) {}
}

class Init{
    static void main()
    { }
}
