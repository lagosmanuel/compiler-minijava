///[Error:m1|11]

class C<P> {

}

class B<V, K> {
    C<K> m1() {}
}
class A<K> extends B<K, Object> {
    C<K> m1() {}
}

class Init{
    static void main()
    { }
}
