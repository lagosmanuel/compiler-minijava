///[SinErrores]
class K {

}

class B<K>{
    K m1() { return null; }
}

class A extends B<K> {
    K m1() { return null; }
}

class Init{
    static void main()
    { }
}