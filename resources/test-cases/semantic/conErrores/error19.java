///[Error:m1|14]

class C {

}

class B<V, C> {
    void m1(C c) {

    }
}

class A extends B<C, Object> {
    void m1(C c) {

    }
}

class Init{
    static void main()
    { }
}