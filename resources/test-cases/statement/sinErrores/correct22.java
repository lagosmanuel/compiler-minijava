//[SinErrores]
class Init{
    static void main() {
        Integer i = 1;
        Float f = 1.0;
        Boolean b = true;
        Character c = 'c';
        int i2 = i + i;
        float f2 = f + f;
        boolean b2 = b || b;
        char c2 = c;

        int i3 = 'a';
        char c3 = 97;
        float f3 = 1;
        String s = 0;
        String s2 = 'a';
        float ff = 'a';

        float f4 = 1.0 + i3;
        f4 += 1;
        f4 += 1.0;
        m1(i3);
        boolean b3 = f4 == i3;
    }

    static void m1(float i) {}
}
