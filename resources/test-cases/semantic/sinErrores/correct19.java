///[SinErrores]
class A<D> {
    public D m1() { return null; }
}

class B<E> extends A<E> {
    public E m1() { return null; }
}

class C<F> extends B<F>{
    public F m1() { return null; }
}

class Init{
    static void main()
    { }
}
