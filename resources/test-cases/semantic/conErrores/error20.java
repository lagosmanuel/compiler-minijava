///[Error:m1|14]

class C<K, V> {

}

class B<K> {
    void m1(C<K, Object> c) {

    }
}

class A extends B<String> {
    void m1(C<System, Object> c) {

    }
}

class Init{
    static void main()
    { }
}
