//[SinErrores]
// probar el this
class Init{
    int x;
    static char c;
    boolean m2() { return false; }
    static void m3() { }

    void m1() {
        boolean b = this.m2();
        int y = this.x;
        char z = this.c;
        m3();
    }

    static void main() {

    }
}
