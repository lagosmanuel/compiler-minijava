//[SinErrores]
class Init{
    void m1() {
        int i = new Integer(1);
        float f = new Float(1.0);
        boolean b = new Boolean(true);
        char c = new Character('a');
    }

    void m2() {
        Integer i = 1;
        Float f = 1.0;
        Boolean b = true;
        Character c = 'a';

        int i2 = i;
        float f2 = f;
        boolean b2 = b;
        char c2 = c;
    }

    static void main() {
    }
}
