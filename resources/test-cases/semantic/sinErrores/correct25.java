///[SinErrores]
class C<T> {

}

class A<L, T> {
    void m1(C<T> t) {}
}

class B<K> extends A<K, Object> {
    void m1(C<Object> t) {}
}

class Init{
    static void main()
    { }
}
